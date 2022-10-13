package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.IPrefix;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class Space extends VSpaceElement implements ISpace {

    @Id
    @GeneratedValue(generator = "space_id_generator")
    @GenericGenerator(name = "space_id_generator", parameters = @Parameter(name = "prefix", value = IPrefix.SPACE_PREFIX), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private SpaceStatus spaceStatus;
    @Access(AccessType.PROPERTY)
    private boolean showUnpublishedLinks;

    private boolean hideIncomingLinks;

    @JsonIgnore
    @OneToMany(mappedBy = "sourceSpace", targetEntity = SpaceLink.class)
    private List<ISpaceLink> spaceLinks;

    @JsonIgnore
    @OneToMany(mappedBy = "space", targetEntity = ModuleLink.class)
    private List<IModuleLink> moduleLinks;

    @JsonIgnore
    @OneToMany(mappedBy = "space", targetEntity = ExternalLink.class)
    private List<IExternalLink> externalLinks;

    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private IVSImage image;

    @Transient
    private Boolean incomingLinks;
    
    @OneToMany(mappedBy = "text", targetEntity = LanguageObject.class)
    private List<ILanguageObject> spaceTitles;
    
    @OneToMany(mappedBy = "text", targetEntity = LanguageObject.class)
    private List<ILanguageObject> spaceDescriptions;
    


    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpaceLinks()
     */
    @Override
    public List<ISpaceLink> getSpaceLinks() {
        return spaceLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISpacee#setSpaceLinks(java.util.List)
     */
    @Override
    public void setSpaceLinks(List<ISpaceLink> spaceLinks) {
        this.spaceLinks = spaceLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getModuleLinks()
     */
    @Override
    public List<IModuleLink> getModuleLinks() {
        return moduleLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISpacee#setModuleLinks(java.util.List)
     */
    @Override
    public void setModuleLinks(List<IModuleLink> moduleLinks) {
        this.moduleLinks = moduleLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getExternalLinks()
     */
    @Override
    public List<IExternalLink> getExternalLinks() {
        return externalLinks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISpacee#setExternalLinks(java.util.
     * List)
     */
    @Override
    public void setExternalLinks(List<IExternalLink> externalLinks) {
        this.externalLinks = externalLinks;
    }

    public IVSImage getImage() {
        return image;
    }

    public void setImage(IVSImage image) {
        this.image = image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.Space#getSpaceStatus()
     */
    @Override
    public SpaceStatus getSpaceStatus() {
        return spaceStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.Space#setSpaceStatus(edu.asu.diging.
     * vspace.core.model.impl.SpaceStatus)
     */
    @Override
    public void setSpaceStatus(SpaceStatus spaceStatus) {
        this.spaceStatus = spaceStatus;
    }

    @Override
    public boolean isShowUnpublishedLinks() {
        return showUnpublishedLinks;
    }

    @Override
    @Access(AccessType.PROPERTY)
    public void setShowUnpublishedLinks(Boolean showUnpublishedLinks) {
        if (showUnpublishedLinks == null) {
            this.showUnpublishedLinks = false;
            return;
        }
        this.showUnpublishedLinks = showUnpublishedLinks;
    }

    public Boolean hasIncomingLinks() {
        return incomingLinks;
    }

    public void setIncomingLinks(Boolean incomingLinks) {
        this.incomingLinks = incomingLinks;
    }

    @Override
    public boolean isHideIncomingLinks() {
        return hideIncomingLinks;
    }


    @Override
    public void setHideIncomingLinks(boolean hideIncomingLinks) {
        this.hideIncomingLinks = hideIncomingLinks;
    }
    
    public List<LanguageObject> getSpaceTitles() {
        return spaceTitles;
    }

    public void setSpaceTitles(List<LanguageObject> spaceTitles) {
        this.spaceTitles = spaceTitles;
    }

    public List<LanguageObject> getSpaceDescriptions() {
        return spaceDescriptions;
    }

    public void setSpaceDescriptions(List<LanguageObject> spaceDescriptions) {
        this.spaceDescriptions = spaceDescriptions;
    }
    
    @Override 
    public void setDescription(String description) {             
        LanguageObject languageObject = new LanguageObject();
        languageObject.setText(description);    
        languageObject.setExhibitionLanguage(null);  
        if(this.getSpaceDescriptions() == null) {
            this.setSpaceDescriptions(new ArrayList());
        }
        this.getSpaceDescriptions().add(languageObject);
    }
    
    @Override 
    public void setName(String title) {
        LanguageObject languageObject = new LanguageObject();
        languageObject.setText(title);    
        languageObject.setExhibitionLanguage(null); 
        if(this.getSpaceTitles() == null) {
            this.setSpaceTitles(new ArrayList());
        }
        this.getSpaceTitles().add(languageObject);
    }

}