package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.util.CitationFormatter;

@Entity
public class TextBlock extends ContentBlock implements ITextBlock {

    @Lob
    private String text;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ITextBlock#getText()
     */
    @Override
    public String getText() {
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ITextBlock#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ITextBlock#htmlRenderedText()
     */
    @Override
    @Transient
    public String htmlRenderedText() {
        String processedText = text;
        
        // First, format citations in the text
        if (processedText != null) {
            processedText = CitationFormatter.formatCitations(processedText);
        }
        
        // Then process with Markdown
        Parser parser = Parser.builder().build();
        Node document = parser.parse(processedText);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlOutput = renderer.render(document);
        
        // Add citation-specific CSS classes for styling
        htmlOutput = addCitationStyling(htmlOutput);
        
        return htmlOutput;
    }
    
    /**
     * Adds CSS classes to citation elements for proper styling
     */
    @Transient
    private String addCitationStyling(String html) {
        if (html == null) {
            return null;
        }
        
        // Add citation class to parenthetical citations
        html = html.replaceAll("\\(([^)]+,\\s*\\d{4}[^)]*)\\)", 
                              "<span class=\"citation in-text-citation\">($1)</span>");
        
        // Add reference class to formatted references (those with italicized titles)
        html = html.replaceAll("(<em>[^<]+</em>[^<]*\\.)", 
                              "<div class=\"citation reference-citation\">$1</div>");
        
        return html;
    }
    
    /**
     * Gets the raw text with citation formatting applied but without HTML conversion
     */
    @Transient
    public String getFormattedText() {
        return text != null ? CitationFormatter.formatCitations(text) : text;
    }
    
    /**
     * Validates if the text block contains properly formatted citations
     */
    @Transient
    public boolean hasValidCitations() {
        return CitationFormatter.hasValidCitations(text);
    }
}
