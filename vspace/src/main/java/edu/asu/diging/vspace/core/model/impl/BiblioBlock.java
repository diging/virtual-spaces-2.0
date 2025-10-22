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
import edu.asu.diging.vspace.core.util.APACitationFormatter;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String biblioTitle;

    private String description;
    
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
    
    /**
     * Renders the bibliography block with APA-formatted references
     * 
     * @return HTML formatted string with APA-style references
     */
    @Override
    @Transient
    public String renderAPAReferences() {
        if (references == null || references.isEmpty()) {
            return "<div class=\"apa-references\"><p>No references available.</p></div>";
        }
        
        StringBuilder result = new StringBuilder();
        result.append("<div class=\"apa-bibliography\">");
        
        // Add bibliography title and description
        if (biblioTitle != null && !biblioTitle.isEmpty()) {
            result.append("<h3 class=\"bibliography-title\"><strong>").append(biblioTitle).append("</strong></h3>");
        }
        
        if (description != null && !description.isEmpty()) {
            result.append("<p class=\"bibliography-description\">").append(description).append("</p>");
        }
        
        // Add APA formatted references
        result.append(APACitationFormatter.formatReferences(references));
        
        result.append("</div>");
        return result.toString();
    }
    
    /**
     * Renders the bibliography block with raw reference data (for editing)
     * 
     * @return HTML formatted string with raw reference data
     */
    @Override
    @Transient
    public String renderRawReferences() {
        if (references == null || references.isEmpty()) {
            return "<div class=\"raw-references\"><p>No references available.</p></div>";
        }
        
        StringBuilder result = new StringBuilder();
        result.append("<div class=\"raw-bibliography\">");
        
        // Add bibliography title and description
        if (biblioTitle != null && !biblioTitle.isEmpty()) {
            result.append("<h3 class=\"bibliography-title\">").append(biblioTitle).append("</h3>");
        }
        
        if (description != null && !description.isEmpty()) {
            result.append("<p class=\"bibliography-description\">").append(description).append("</p>");
        }
        
        // Add raw reference data
        result.append("<div class=\"raw-references\">");
        for (int i = 0; i < references.size(); i++) {
            Reference ref = references.get(i);
            result.append("<div class=\"raw-reference\" data-ref-id=\"").append(i).append("\">");
            result.append(formatRawReference(ref));
            result.append("</div>");
        }
        result.append("</div>");
        
        result.append("</div>");
        return result.toString();
    }
    
    /**
     * Formats a single reference as raw data for editing
     */
    private String formatRawReference(Reference ref) {
        StringBuilder result = new StringBuilder();
        
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            result.append("Reference Title: ").append(ref.getTitle()).append(", ");
        }
        
        if (ref.getAuthor() != null && !ref.getAuthor().isEmpty()) {
            result.append("Author: ").append(ref.getAuthor()).append(", ");
        }
        
        if (ref.getYear() != null && !ref.getYear().isEmpty()) {
            result.append("Year: ").append(ref.getYear()).append(", ");
        }
        
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            result.append("Journal: ").append(ref.getJournal()).append(", ");
        }
        
        if (ref.getUrl() != null && !ref.getUrl().isEmpty()) {
            result.append("Url: ").append(ref.getUrl()).append(", ");
        }
        
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            result.append("Volume: ").append(ref.getVolume()).append(", ");
        }
        
        if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            result.append("Issue: ").append(ref.getIssue()).append(", ");
        }
        
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            result.append("Pages: ").append(ref.getPages()).append(", ");
        }
        
        if (ref.getEditors() != null && !ref.getEditors().isEmpty()) {
            result.append("Editors: ").append(ref.getEditors()).append(", ");
        }
        
        if (ref.getType() != null && !ref.getType().isEmpty()) {
            result.append("Type: ").append(ref.getType()).append(", ");
        }
        
        if (ref.getNote() != null && !ref.getNote().isEmpty()) {
            result.append("Note: ").append(ref.getNote());
        }
        
        return result.toString();
    }

}
