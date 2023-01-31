package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;

@Entity
public class LanguageDescriptionObject implements ILanguageDescriptionObject {
    
    
    @Id
    @GeneratedValue(generator = "language_description_id_generator")
    @GenericGenerator(name = "language_description_id_generator", parameters = @Parameter(name = "prefix", value = "EXHLANGOBJ"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
   
    @OneToOne(targetEntity = ExhibitionLanguage.class)
    ExhibitionLanguage exhibitionLanguage; 

    String text;

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


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


}
