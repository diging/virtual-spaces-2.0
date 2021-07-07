package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IBiblioBlock;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String biblioTitle;

    private String description;
    
    @JsonIgnore    
    @ManyToMany
//    @JoinTable(name = "biblio_reference"
//    , joinColumns =
//    @JoinColumn(name = "BIBLIO_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "REFERENCE_ID", referencedColumnName = "id"))
    private List<Reference> referenceList;

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
    public void setReferenceList(List<Reference> referenceList) {
        this.referenceList = referenceList;
    }

    @Override
    public List<Reference> getReferenceList() {
        return referenceList;
    }
    
    @Override
    @Transient
    public String toString() {
        return "Bibliography title=" + biblioTitle + ", description=" + description ;
    }
    
    @Transient
    public String htmlRenderedBiblio() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(toString());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
