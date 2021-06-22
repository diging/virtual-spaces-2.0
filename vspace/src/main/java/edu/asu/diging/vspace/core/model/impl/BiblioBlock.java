package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String biblioTitle;

    private String description;
    
  //-------- @JsonIgnore used as this Reference will be returned in a controller
    @JsonIgnore
    @OneToMany(targetEntity = Reference.class, mappedBy = "biblio")
    private List<IReference> referenceList;

    @Override
    public String getBiblioTitle() {
        return biblioTitle;
    }

    @Override
    public void setBiblioTitle(String biblioTitle) {
        this.biblioTitle = biblioTitle;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setReferenceList(List<IReference> referenceList) {
        this.referenceList = referenceList;
    }

    @Override
    public List<IReference> getReferenceList() {
        return referenceList;
    }

}
