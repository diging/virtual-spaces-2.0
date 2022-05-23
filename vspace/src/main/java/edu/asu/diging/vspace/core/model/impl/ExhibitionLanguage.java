package edu.asu.diging.vspace.core.model.impl;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class ExhibitionLanguage {

    public ExhibitionLanguage() {
        super();
    }

    @Id
    @GeneratedValue(generator = "exhibit_language_id_generator")
    @GenericGenerator(name = "exhibit_language_id_generator", parameters = @Parameter(name = "prefix", value = "EXH"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    private String label;
    
    @ManyToOne(targetEntity = Exhibition.class)
    private Exhibition exhibition;
    
    private String code;
    
    private boolean isDefault;

    public ExhibitionLanguage(String label, String code, Exhibition exhibition) {
       this.label=label;
       this.code=code;
       this.exhibition=exhibition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
    

    @Override
    public int hashCode() {
        return Objects.hash(code, exhibition, label);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExhibitionLanguage other = (ExhibitionLanguage) obj;
        return Objects.equals(code, other.code) && Objects.equals(exhibition, other.exhibition)
               && Objects.equals(label, other.label);
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
