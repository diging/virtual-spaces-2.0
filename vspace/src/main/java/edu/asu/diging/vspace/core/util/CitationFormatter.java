package edu.asu.diging.vspace.core.util;

import java.util.List;
import edu.asu.diging.vspace.core.model.impl.Reference;

public class APACitationFormatter {
    
    /**
     * Formats a list of references in APA style
     * 
     * @param references List of Reference objects to format
     * @return HTML formatted string with APA-style references
     */
    public static String formatReferences(List<Reference> references) {
        if (references == null || references.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        result.append("<div class=\"apa-references\">");
        
        for (int i = 0; i < references.size(); i++) {
            Reference ref = references.get(i);
            result.append(formatSingleReference(ref, i + 1));
        }
        
        result.append("</div>");
        return result.toString();
    }
    
    /**
     * Formats a single reference in APA style
     */
    private static String formatSingleReference(Reference ref, int index) {
        StringBuilder citation = new StringBuilder();
        citation.append("<div class=\"apa-reference\" id=\"ref-").append(index).append("\">");
        
        // Author(s)
        String authors = formatAuthors(ref.getAuthor());
        if (authors != null && !authors.isEmpty()) {
            citation.append(authors).append(" ");
        }
        
        // Year
        if (ref.getYear() != null && !ref.getYear().isEmpty()) {
            citation.append("(").append(ref.getYear()).append("). ");
        }
        
        // Title
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            if (!title.endsWith(".")) {
                title += ".";
            }
            citation.append("<em>").append(title).append("</em> ");
        }
        
        // Journal/Publication info
        if (ref.getType() != null) {
            String type = ref.getType().toLowerCase();
            switch (type) {
                case "journal article":
                    formatJournalArticle(ref, citation);
                    break;
                case "book":
                    formatBook(ref, citation);
                    break;
                case "thesis":
                    formatThesis(ref, citation);
                    break;
                case "patent":
                    formatPatent(ref, citation);
                    break;
                case "report":
                    formatReport(ref, citation);
                    break;
                default:
                    formatGeneric(ref, citation);
                    break;
            }
        } else {
            formatGeneric(ref, citation);
        }
        
        // URL
        if (ref.getUrl() != null && !ref.getUrl().isEmpty()) {
            citation.append(" Retrieved from ").append(ref.getUrl());
        }
        
        citation.append("</div>");
        return citation.toString();
    }
    
    /**
     * Formats author names in APA style
     */
    private static String formatAuthors(String author) {
        if (author == null || author.isEmpty()) {
            return "";
        }
        
        String[] authors = author.split(",");
        StringBuilder formatted = new StringBuilder();
        
        for (int i = 0; i < authors.length; i++) {
            String name = authors[i].trim();
            if (name.isEmpty()) continue;
            
            String[] parts = name.split("\\s+");
            if (parts.length >= 2) {
                // Last name, First initial
                formatted.append(parts[parts.length - 1]).append(", ");
                for (int j = 0; j < parts.length - 1; j++) {
                    if (parts[j].length() > 0) {
                        formatted.append(parts[j].charAt(0)).append(". ");
                    }
                }
            } else {
                formatted.append(name);
            }
            
            if (i < authors.length - 1) {
                formatted.append(", ");
            }
        }
        
        return formatted.toString().trim();
    }
    
    /**
     * Formats journal article references
     */
    private static void formatJournalArticle(Reference ref, StringBuilder citation) {
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append("<em>").append(ref.getJournal()).append("</em>");
        }
        
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(", ").append(ref.getVolume());
        }
        
        if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            citation.append("(").append(ref.getIssue()).append(")");
        }
        
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            citation.append(", ").append(ref.getPages());
        }
        
        citation.append(".");
    }
    
    /**
     * Formats book references
     */
    private static void formatBook(Reference ref, StringBuilder citation) {
        if (ref.getEditors() != null && !ref.getEditors().isEmpty()) {
            citation.append("(Ed.), ");
        }
        
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append("Vol. ").append(ref.getVolume()).append(". ");
        }
        
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            citation.append("pp. ").append(ref.getPages()).append(". ");
        }
    }
    
    /**
     * Formats thesis references
     */
    private static void formatThesis(Reference ref, StringBuilder citation) {
        citation.append("Unpublished ");
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append(ref.getJournal()).append(" ");
        }
        citation.append("thesis.");
    }
    
    /**
     * Formats patent references
     */
    private static void formatPatent(Reference ref, StringBuilder citation) {
        citation.append("U.S. Patent No. ");
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(ref.getVolume());
        }
        citation.append(".");
    }
    
    /**
     * Formats report references
     */
    private static void formatReport(Reference ref, StringBuilder citation) {
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append("(").append(ref.getJournal()).append("). ");
        }
        citation.append("Report.");
    }
    
    /**
     * Formats generic references
     */
    private static void formatGeneric(Reference ref, StringBuilder citation) {
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append(ref.getJournal()).append(".");
        }
        
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(" ").append(ref.getVolume());
        }
        
        if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            citation.append("(").append(ref.getIssue()).append(")");
        }
        
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            citation.append(", ").append(ref.getPages());
        }
        
        citation.append(".");
    }
} 