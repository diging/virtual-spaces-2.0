package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;

/**
 * Represents an exhibition that can have a default start space.
 * 
 * @see edu.asu.diging.vspace.core.model.IExhibition
 * @author Namratha
 */
@Entity
public class Exhibition extends VSpaceElement implements IExhibition {

    @Id
    @GeneratedValue(generator = "exhibit_id_generator")
    @GenericGenerator(name = "exhibit_id_generator", parameters = @Parameter(name = "prefix", value = "EXH"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Space.class)
    private ISpace startSpace;

    private String title;

    @Enumerated(EnumType.STRING)
    private ExhibitionModes mode;

    private String customMessage;
    
    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private IVSImage spacelinkImage;
    
    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private IVSImage modulelinkImage;
    
    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private IVSImage externallinkImage;
    
    
    
    private boolean aboutPageConfigured;
    
    @OneToMany(targetEntity = ExhibitionLanguage.class, mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<IExhibitionLanguage> languages = new ArrayList<IExhibitionLanguage>();

    private String previewId;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.IVSpaceElement#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.IVSpaceElement#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.IExhibition#getSpace()
     */
    @Override
    public ISpace getStartSpace() {
        return this.startSpace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.IExhibition#setSpace(edu.asu.diging.vspace.
     * core.model.ISpace)
     */
    @Override
    public void setStartSpace(ISpace space) {
        this.startSpace = space;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }


    public ExhibitionModes getMode() {

        return mode;
    }

    public void setMode(ExhibitionModes mode) {
        this.mode = mode;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
    
    @Override
    public boolean isAboutPageConfigured() {
        return aboutPageConfigured;
    }
    
    @Override
    public void setAboutPageConfigured(boolean aboutPageConfigured) {
        this.aboutPageConfigured = aboutPageConfigured;
    }


    public IVSImage getModulelinkImage() {
        return modulelinkImage;
    }

    public IVSImage getSpacelinkImage() {
        return spacelinkImage;
    }

    public void setModulelinkImage(IVSImage modulelinkImage) {
        this.modulelinkImage = modulelinkImage;
    }

    public IVSImage getExternallinkImage() {
        return externallinkImage;
    }

    public void setExternallinkImage(IVSImage externallinkImage) {
        this.externallinkImage = externallinkImage;
    }

    public void setSpacelinkImage(IVSImage spacelinkImage) {
        this.spacelinkImage = spacelinkImage;
    }

	
    


    public List<IExhibitionLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<IExhibitionLanguage> languages) {
        this.languages = languages;
    }
   
    public String getPreviewId() {
        return previewId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return Objects.equals(id, ((Exhibition) obj).id);
    }

}
