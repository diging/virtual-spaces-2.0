package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpaceTitle;

@Entity
public class SpaceTitle extends VSpaceElement implements ISpaceTitle {
    
    @Id
    @GeneratedValue(generator = "space_title_id_generator")
    @GenericGenerator(name = "space_title_id_generator", parameters = @Parameter(name = "prefix", value = "SPCTTL"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @OneToOne(targetEntity = ExhibitionLanguage.class)
    private ExhibitionLanguage exhibitionLanguage; 
    
    private String title;
    
    @ManyToOne(targetEntity = Space.class)
    private Space space;

    
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
        
    }
    
 
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }

    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }

 
}


