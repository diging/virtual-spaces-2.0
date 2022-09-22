package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpaceDescription;

@Entity
public class SpaceDescription extends VSpaceElement implements ISpaceDescription {

    
    @Id
    @GeneratedValue(generator = "space_description_id_generator")
    @GenericGenerator(name = "space_description_id_generator", parameters = @Parameter(name = "prefix", value = "SPCDES"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @OneToOne(targetEntity = ExhibitionLanguage.class)
    private ExhibitionLanguage exhibitionLanguage; 
    
    String description;
    
    @ManyToOne(targetEntity = Space.class)
    private Space space;
    
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
        
    }

    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }

    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }
    

}
