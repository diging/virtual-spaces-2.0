package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ExhibitionLanguageCodes;

@Entity
public class ExhibitionLanguage {

    @Id
    @GeneratedValue(generator = "exhibit_language_id_generator")
    @GenericGenerator(name = "exhibit_language_id_generator", parameters = @Parameter(name = "prefix", value = "EXH"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    private String label;
    
    @ManyToOne(targetEntity = Exhibition.class)
    private Exhibition exhibition;
    
    @Enumerated(EnumType.STRING)
    private ExhibitionLanguageCodes code;

    public ExhibitionLanguageCodes getCode() {
        return code;
    }

    public void setCode(ExhibitionLanguageCodes code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }
    
}
