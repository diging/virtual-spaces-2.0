package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IBiblioBlock;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String biblioTitle;

    @Lob
    private String description;
    
    @JsonIgnore    
    @ManyToMany
    @JoinTable(name = "Biblio_Reference", joinColumns = @JoinColumn(name = "BIBLIO_ID"), 
        inverseJoinColumns = @JoinColumn(name = "REFERENCE_ID"))
    private List<Reference> references;

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
    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    @Override
    public List<Reference> getReferences() {
        return references;
    }
    
    @Override
    public String toString() {
        return "Bibliography title=" + biblioTitle + ", description=" + description ;
    }

}
