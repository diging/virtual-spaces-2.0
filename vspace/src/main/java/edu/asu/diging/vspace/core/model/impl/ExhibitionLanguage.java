package edu.asu.diging.vspace.core.model.impl;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;

@Entity
public class ExhibitionLanguage extends VSpaceElement implements IExhibitionLanguage {

    @Id
    @GeneratedValue(generator = "exhibit_language_id_generator")
    @GenericGenerator(name = "exhibit_language_id_generator", parameters = @Parameter(name = "prefix", value = "LANG"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    private String label;
    
    @ManyToOne(targetEntity = Exhibition.class, fetch = FetchType.LAZY)
    private IExhibition exhibition;
    
    private String code;
    
    private boolean isDefault;
    
    public ExhibitionLanguage() {
        super();
    }
    
    public ExhibitionLanguage(String label, String code, IExhibition exhibition) {
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
    	this.id = id;      
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public IExhibition getExhibition() {
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
        return Objects.hash(code, 
                exhibition == null ? null : exhibition.getId()
            );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        ExhibitionLanguage other = (ExhibitionLanguage) obj;
        
        if (!Objects.equals(this.code, other.code))
        	return false;
        
        if (this.exhibition == null || other.exhibition == null)
        	return false;
        
        return Objects.equals(this.exhibition.getId(),other.exhibition.getId());
    }


}
