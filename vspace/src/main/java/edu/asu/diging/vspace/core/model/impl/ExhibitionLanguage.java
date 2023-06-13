package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;

@Entity
public class ExhibitionLanguage extends VSpaceElement implements IExhibitionLanguage {

    @Id
    @GeneratedValue(generator = "exhibit_language_id_generator")
    @GenericGenerator(name = "exhibit_language_id_generator", parameters = @Parameter(name = "prefix", value = "LANG"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    private String label;
    
    @ManyToOne(targetEntity = Exhibition.class)
    private Exhibition exhibition;
    
    private String code;
    
    private boolean isDefault;
    
    
    public ExhibitionLanguage() {
        super();
    }
    
    public ExhibitionLanguage(String label, String code, Exhibition exhibition) {
        this.label=label;
        this.code=code;
        this.exhibition=exhibition;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.setId(id);        
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
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
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


}