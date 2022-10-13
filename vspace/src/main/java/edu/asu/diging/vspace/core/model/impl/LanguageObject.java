package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class LanguageObject implements ILanguageObject {
    
    
    @Id
    @GeneratedValue(generator = "language_object_id_generator")
    @GenericGenerator(name = "language_object_id_generator", parameters = @Parameter(name = "prefix", value = "LANGOBJ"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    
    @OneToOne(targetEntity = ExhibitionLanguage.class)
    private ExhibitionLanguage exhibitionLanguage; 

    String text;



    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }


    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


}
