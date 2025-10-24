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
     * Formats a single reference as raw data for editing.
     * 
     * Creates a human-readable string representation of all reference fields
     * in the format "Label: Value, Label: Value, ..."
     * Only non-empty fields are included in the output.
     * 
     * @param ref The reference to format
     * @return A formatted string with all available reference information
     */
    private String formatRawReference(Reference ref) {
        StringBuilder result = new StringBuilder();
        
        // Append each field if it has a value
        // The third parameter (true/false) controls whether to add a comma after the field
        appendFieldIfPresent(result, "Reference Title", ref.getTitle(), true);
        appendFieldIfPresent(result, "Author", ref.getAuthor(), true);
        appendFieldIfPresent(result, "Year", ref.getYear(), true);
        appendFieldIfPresent(result, "Journal", ref.getJournal(), true);
        appendFieldIfPresent(result, "Url", ref.getUrl(), true);
        appendFieldIfPresent(result, "Volume", ref.getVolume(), true);
        appendFieldIfPresent(result, "Issue", ref.getIssue(), true);
        appendFieldIfPresent(result, "Pages", ref.getPages(), true);
        appendFieldIfPresent(result, "Editors", ref.getEditors(), true);
        appendFieldIfPresent(result, "Type", ref.getType(), true);
        // Note is the last field, so no comma is added after it
        appendFieldIfPresent(result, "Note", ref.getNote(), false);
        
        return result.toString();
    }
    
    /**
     * Helper method to append a field to the result string if the field has a value.
     * 
     * This method encapsulates the null-checking and formatting logic to reduce
     * code duplication and improve maintainability. It only appends the field
     * if the value is not null and not empty.
     * 
     * @param result StringBuilder to append the formatted field to
     * @param label The display label for the field (e.g., "Author", "Year")
     * @param value The field value to check and append
     * @param addComma Whether to add a comma separator after the value (use false for last field)
     */
    private void appendFieldIfPresent(StringBuilder result, String label, String value, boolean addComma) {
        // Only append if the value exists and is not empty
        if (value != null && !value.isEmpty()) {
            // Format: "Label: Value"
            result.append(label).append(": ").append(value);
            // Add comma separator if this is not the last field
            if (addComma) {
                result.append(", ");
            }
        }
    }

}
