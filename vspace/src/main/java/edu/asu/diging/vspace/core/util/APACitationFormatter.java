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
        
        // Determine type first as it affects formatting
        String type = (ref.getType() != null) ? ref.getType().toLowerCase() : "generic";
        
        // Author(s)
        String authors = formatAuthors(ref.getAuthor());
        if (authors != null && !authors.isEmpty()) {
            citation.append(authors);
            if (!authors.endsWith(".")) {
                citation.append(".");
            }
            citation.append(" ");
        }
        
        // Year
        if (ref.getYear() != null && !ref.getYear().isEmpty()) {
            citation.append("(").append(ref.getYear()).append("). ");
        }
        
        // Format based on type (different types have different title/content formatting)
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
            case "website":
                formatWebsite(ref, citation);
                break;
            default:
                formatGeneric(ref, citation);
                break;
        }
        
        // URL (if not already included in type-specific formatting)
        if (ref.getUrl() != null && !ref.getUrl().isEmpty() && 
            !type.equals("website")) {
            citation.append(" Retrieved from ").append(ref.getUrl());
        }
        
        citation.append("</div>");
        return citation.toString();
    }
    
    /**
     * Formats author names in APA style
     * Handles formats like "First Last", "Last, First", or "First Middle Last"
     * Multiple authors can be separated by semicolons or " and "
     */
    private static String formatAuthors(String author) {
        if (author == null || author.isEmpty()) {
            return "";
        }
        
        // Split by semicolon or " and " to handle multiple authors
        String[] authors = author.split(";|\\s+and\\s+");
        StringBuilder formatted = new StringBuilder();
        
        for (int i = 0; i < authors.length; i++) {
            String name = authors[i].trim();
            if (name.isEmpty()) continue;
            
            // Check if already in "Last, First" format
            if (name.contains(",")) {
                String[] parts = name.split(",", 2);
                String lastName = parts[0].trim();
                String firstName = parts[1].trim();
                
                formatted.append(lastName).append(", ");
                // Add initials from first name
                String[] firstParts = firstName.split("\\s+");
                for (String part : firstParts) {
                    if (part.length() > 0) {
                        formatted.append(part.charAt(0)).append(".");
                        if (part.length() == 1 || Character.isUpperCase(part.charAt(0))) {
                            formatted.append(" ");
                        }
                    }
                }
            } else {
                // Assume "First Middle Last" format
                String[] parts = name.split("\\s+");
                if (parts.length >= 2) {
                    // Last name is the final part
                    formatted.append(parts[parts.length - 1]).append(", ");
                    // First/middle names as initials
                    for (int j = 0; j < parts.length - 1; j++) {
                        if (parts[j].length() > 0) {
                            formatted.append(parts[j].charAt(0)).append(". ");
                        }
                    }
                } else {
                    // Single name (organization or mononym)
                    formatted.append(name);
                }
            }
            
            // Add separator between authors
            if (i < authors.length - 2) {
                formatted.append(", ");
            } else if (i == authors.length - 2) {
                formatted.append(", & ");
            }
        }
        
        return formatted.toString().trim();
    }
    
    /**
     * Formats journal article references
     * APA format: Article title. Journal Name, volume(issue), pages.
     */
    private static void formatJournalArticle(Reference ref, StringBuilder citation) {
        // Article title (NOT italicized for journal articles)
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append(title);
            if (!title.endsWith(".")) {
                citation.append(".");
            }
            citation.append(" ");
        }
        
        // Journal name (italicized)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append("<em>").append(ref.getJournal()).append("</em>");
        }
        
        // Volume (italicized with journal)
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(", <em>").append(ref.getVolume()).append("</em>");
        }
        
        // Issue (in parentheses, not italicized)
        if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            citation.append("(").append(ref.getIssue()).append(")");
        }
        
        // Pages
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            citation.append(", ").append(ref.getPages());
        }
        
        citation.append(".");
    }
    
    /**
     * Formats book references
     * APA format: Book title (Vol. X, pp. Y-Z). Location: Publisher.
     */
    private static void formatBook(Reference ref, StringBuilder citation) {
        // Book title (italicized)
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append("<em>").append(title).append("</em>");
        }
        
        // Volume and pages in parentheses
        StringBuilder extras = new StringBuilder();
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            extras.append("Vol. ").append(ref.getVolume());
        }
        
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            if (extras.length() > 0) {
                extras.append(", ");
            }
            extras.append("pp. ").append(ref.getPages());
        }
        
        if (extras.length() > 0) {
            citation.append(" (").append(extras).append(")");
        }
        
        citation.append(".");
        
        // Editors (if mentioned separately from authors)
        if (ref.getEditors() != null && !ref.getEditors().isEmpty()) {
            citation.append(" ").append(ref.getEditors()).append(" (Ed.).");
        }
        
        // Publisher info from journal field (often used for publisher)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append(" ").append(ref.getJournal()).append(".");
        }
    }
    
    /**
     * Formats thesis references
     * APA format: Title (Unpublished doctoral/master's dissertation). Institution.
     */
    private static void formatThesis(Reference ref, StringBuilder citation) {
        // Title (italicized)
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append("<em>").append(title).append("</em>");
        }
        
        citation.append(" (Unpublished");
        if (ref.getNote() != null && !ref.getNote().isEmpty()) {
            citation.append(" ").append(ref.getNote());
        } else {
            citation.append(" doctoral dissertation");
        }
        citation.append(").");
        
        // Institution (from journal field)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append(" ").append(ref.getJournal()).append(".");
        }
    }
    
    /**
     * Formats patent references
     * APA format: Title (U.S. Patent No. XXXXX).
     */
    private static void formatPatent(Reference ref, StringBuilder citation) {
        // Title (italicized)
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append("<em>").append(title).append("</em>");
        }
        
        citation.append(" (U.S. Patent No. ");
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(ref.getVolume());
        } else if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            citation.append(ref.getIssue());
        }
        citation.append(").");
    }
    
    /**
     * Formats report references
     * APA format: Title (Report No. XXX). Publisher.
     */
    private static void formatReport(Reference ref, StringBuilder citation) {
        // Title (italicized)
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append("<em>").append(title).append("</em>");
        }
        
        // Report number
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(" (Report No. ").append(ref.getVolume()).append(")");
        }
        
        citation.append(".");
        
        // Publisher (from journal field)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append(" ").append(ref.getJournal()).append(".");
        }
    }
    
    /**
     * Formats website references
     * APA format: Title. URL
     */
    private static void formatWebsite(Reference ref, StringBuilder citation) {
        // Title (NOT italicized for web pages)
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append(title);
            if (!title.endsWith(".")) {
                citation.append(".");
            }
        }
        
        // Website name
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append(" <em>").append(ref.getJournal()).append("</em>.");
        }
        
        // URL (always included for websites)
        if (ref.getUrl() != null && !ref.getUrl().isEmpty()) {
            citation.append(" Retrieved from ").append(ref.getUrl());
        }
    }
    
    /**
     * Formats generic references
     * Fallback for unknown types
     */
    private static void formatGeneric(Reference ref, StringBuilder citation) {
        // Title
        if (ref.getTitle() != null && !ref.getTitle().isEmpty()) {
            String title = ref.getTitle().trim();
            citation.append(title);
            if (!title.endsWith(".")) {
                citation.append(".");
            }
            citation.append(" ");
        }
        
        // Source/Journal
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            citation.append("<em>").append(ref.getJournal()).append("</em>");
        }
        
        // Volume
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            citation.append(", ").append(ref.getVolume());
        }
        
        // Issue
        if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            citation.append("(").append(ref.getIssue()).append(")");
        }
        
        // Pages
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            citation.append(", ").append(ref.getPages());
        }
        
        citation.append(".");
    }
} 