package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlideExternalLink;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;

@Entity
public class Slide extends VSpaceElement implements ISlide {

    @Id
    @GeneratedValue(generator = "slide_id_generator")
    @GenericGenerator(name = "slide_id_generator", parameters = @Parameter(name = "prefix", value = "SLI"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @ManyToOne(targetEntity = Module.class)
    private IModule module;

    // -------- @JsonIgnore used as this entity will be returned in a controller
    @JsonIgnore
    @OneToMany(targetEntity = ContentBlock.class, mappedBy = "slide", cascade = CascadeType.ALL)
    private List<IContentBlock> contents;

    @JsonIgnore
    @ManyToMany(mappedBy = "slides", targetEntity = Sequence.class)
    private List<ISequence> sequence;
    
    @OneToMany(targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="Slide_LangObj_names",  
        joinColumns = @JoinColumn(name = "Slide_Id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name = "LocalizedText_Id", referencedColumnName="id"))
    private List<ILocalizedText> slideNames = new ArrayList<ILocalizedText>();

    @OneToMany(targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="Slide_LangObj_descriptions",
        joinColumns = @JoinColumn(name = "Slide_Id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name = "LocalizedText_Id", referencedColumnName="id"))
    private List<ILocalizedText> slideDescriptions = new ArrayList<ILocalizedText>();
    
    public List<ILocalizedText> getSlideNames() {
        return slideNames;
    }

    public void setSlideNames(List<ILocalizedText> slideNames) {
        this.slideNames = slideNames;
    }

    public List<ILocalizedText> getSlideDescriptions() {
        return slideDescriptions;
    }

    public void setSlideDescriptions(List<ILocalizedText> slideDescriptions) {
        this.slideDescriptions = slideDescriptions;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "slide", targetEntity = SlideExternalLink.class, cascade = CascadeType.ALL)
    private List<ISlideExternalLink> externalLinks;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getModule()
     */
    @Override
    public IModule getModule() {
        return module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#setImage(edu.asu.diging.
     * vspace. core.model.IModule)
     */
    @Override
    public void setModule(IModule module) {
        this.module = module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getExternalLinks()
     */
    @Override
    public List<ISlideExternalLink> getExternalLinks() {
        if (externalLinks == null) {
            externalLinks = new ArrayList<ISlideExternalLink>();
        }
        return externalLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#setExternalLinks(java.util.
     * List)
     */
    @Override
    public void setExternalLinks(List<ISlideExternalLink> externalLinks) {
        this.externalLinks = externalLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getContents()
     */
    @Override
    public List<IContentBlock> getContents() {
        Collections.sort(this.contents, new Comparator<IContentBlock>() {

            @Override
            public int compare(IContentBlock o1, IContentBlock o2) {
                return o1.getContentOrder().compareTo(o2.getContentOrder());
            }
        });
        return contents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#setContents(edu.asu.diging.
     * vspace. core.model.IContentBlock)
     */
    @Override
    public void setContents(List<IContentBlock> contents) {
        this.contents = contents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getContents()
     */
    public List<ISequence> getSequence() {
        return sequence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#setSequence(java.util.List)
     */
    public void setSequence(List<ISequence> sequence) {
        this.sequence = sequence;
    }

    /**
     * This Method will retrieve the first ImageBlock of a slide if the ImageBlock
     * is present
     * 
     * @return IImageBlock
     */
    @Override
    @JsonIgnore
    public IImageBlock getFirstImageBlock() {
        List<IContentBlock> allBlocks = getContents();
        if (allBlocks != null) {
            Optional<IContentBlock> firstImageBlock = allBlocks.stream()
                    .filter(contentBlock -> contentBlock instanceof ImageBlock).findFirst();
            if (firstImageBlock.isPresent()) {
                return (ImageBlock) firstImageBlock.get();
            }
        }
        return null;
    }

    /**
     * This Method will return the first Text block whose content has searchTerm in
     * it.
     * 
     * @param searchTerm the search string which is being searched.
     * @return TextBlock
     */
    @Override
    @JsonIgnore
    public ITextBlock getFirstMatchedTextBlock(String searchTerm) {
        List<IContentBlock> allBlocks = getContents();
        if (allBlocks != null) {
            Optional<IContentBlock> firstMatchedTextBlock = allBlocks.stream()
                    .filter(contentBlock -> contentBlock instanceof TextBlock)
                    .filter(contentBlock -> ((TextBlock) contentBlock).getText().contains(searchTerm)).findFirst();
            if (firstMatchedTextBlock.isPresent()) {
                return (TextBlock) firstMatchedTextBlock.get();
            }
        }
        return null;
    }
    
    @Override
    public String getLocalizedName(String languageCode, String defaultLanguageCode) {
        if (slideNames == null || slideNames.isEmpty()) {
            return getName() != null ? getName() : "";
        }

        if (languageCode != null && !languageCode.trim().isEmpty()) {
            for (ILocalizedText localizedText : slideNames) {
                if (localizedText.getExhibitionLanguage() != null && 
                    languageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        if (defaultLanguageCode != null && !defaultLanguageCode.trim().isEmpty() && !defaultLanguageCode.equals(languageCode)) {
            for (ILocalizedText localizedText : slideNames) {
                if (localizedText.getExhibitionLanguage() != null && 
                    defaultLanguageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }

        
        return getName() != null ? getName() : "";
    }
    
    @Override
    public String getLocalizedDescription(String languageCode, String defaultLanguageCode) {
        if (slideDescriptions == null || slideDescriptions.isEmpty()) {
            return getDescription() != null ? getDescription() : "";
        }
        
        // First, try to find text in the requested language
        if (languageCode != null && !languageCode.trim().isEmpty()) {
            for (ILocalizedText localizedText : slideDescriptions) {
                if (localizedText.getExhibitionLanguage() != null && 
                    languageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        // If not found, try default language
        if (defaultLanguageCode != null && !defaultLanguageCode.trim().isEmpty() && !defaultLanguageCode.equals(languageCode)) {
            for (ILocalizedText localizedText : slideDescriptions) {
                if (localizedText.getExhibitionLanguage() != null && 
                    defaultLanguageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        // Last resort: return the first available text or the base description
        for (ILocalizedText localizedText : slideDescriptions) {
            if (localizedText.getText() != null && !localizedText.getText().trim().isEmpty()) {
                return localizedText.getText();
            }
        }
        
        return getDescription() != null ? getDescription() : "";
    }
}
