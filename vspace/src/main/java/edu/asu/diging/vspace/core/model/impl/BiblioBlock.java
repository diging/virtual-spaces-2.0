package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IBiblioBlock;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String biblioTitle;

    @JsonIgnore    
    @ManyToMany
    @JoinTable(name = "Biblio_Reference", joinColumns = @JoinColumn(name = "BIBLIO_ID"), inverseJoinColumns = @JoinColumn(name = "REFERENCE_ID"))
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
    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    @Override
    public List<Reference> getReferences() {
        return references;
    }

}
