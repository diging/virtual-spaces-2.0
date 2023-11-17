package edu.asu.diging.vspace.core.model.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;

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
    
    @JsonIgnore
    @OneToMany(mappedBy = "slide", targetEntity = ExternalLinkSlide.class)
    private List<IExternalLinkSlide> externalLinks;
    

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
    public List<IExternalLinkSlide> getExternalLinks() {
        return externalLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISlide#setExternalLinks(java.util.
     * List)
     */
    @Override
    public void setExternalLinks(List<IExternalLinkSlide> externalLinks) {
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
}
