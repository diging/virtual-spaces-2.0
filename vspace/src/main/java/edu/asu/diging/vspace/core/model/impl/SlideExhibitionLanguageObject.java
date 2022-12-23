package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import edu.asu.diging.vspace.core.model.ISlideExhibitionLanguageObject;

@Entity
public class SlideExhibitionLanguageObject implements ISlideExhibitionLanguageObject{
    
    @Id
    @GeneratedValue(generator = "slide_language_id_generator")
    @GenericGenerator(name = "slide_language_id_generator", parameters = @Parameter(name = "prefix", value = "SLIDELANGOBJ"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = ExhibitionLanguage.class)
    ExhibitionLanguage exhibitionLanguage; 

    String slideText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }

    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }

    public String getSlideText() {
        return slideText;
    }

    public void setSlideText(String slideText) {
        this.slideText = slideText;
    }

   
    
    
}
