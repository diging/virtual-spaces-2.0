package edu.asu.diging.vspace.core.util;

import java.util.List;
import edu.asu.diging.vspace.core.model.impl.Reference;

/**
 * @deprecated Use IReferenceMetadataRegistry with ReferenceMetadataType.APA instead.
 * This class is kept for backward compatibility but will be removed in a future version.
 * The new architecture uses providers registered with the IReferenceMetadataRegistry,
 * which use ReferenceDisplayFormatter to build citations.
 *
 * @see edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry
 * @see edu.asu.diging.vspace.core.references.impl.APAReferenceMetadataProvider
 * @see edu.asu.diging.vspace.core.references.ReferenceDisplayFormatter
 */
@Deprecated
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
            result.append(formatSingleReferenceInternal(ref, i + 1));
        }

        result.append("</div>");
        return result.toString();
    }

    /**
     * Formats a single reference in APA style (public method)
     *
     * @param ref The reference to format
     * @return HTML formatted string with APA-style citation
     */
    public static String formatSingleReference(Reference ref) {
        if (ref == null) {
            return "";
        }
        return formatSingleReferenceInternal(ref, 1);
    }

    /**
     * Formats a single reference in APA style (internal method with index)
     */
    private static String formatSingleReferenceInternal(Reference ref, int index) {
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
     * Formats author names in APA style.
     * 
     * Handles various input formats:
     * - "First Last" or "First Middle Last" (converted to "Last, F. M.")
     * - "Last, First" (converted to "Last, F.")
     * - Multiple authors separated by semicolons or " and "
     * 
     * Output format follows APA style:
     * - Single author: "Last, F. M."
     * - Two authors: "Last1, F. M., & Last2, F. M."
     * - Three+ authors: "Last1, F. M., Last2, F. M., & Last3, F. M."
     * 
     * @param author The author string to format
     * @return APA-formatted author string, or empty string if input is null/empty
     */
    private static String formatAuthors(String author) {
        if (author == null || author.isEmpty()) {
            return "";
        }
        
        // Split by semicolon or " and " to handle multiple authors
        String[] authors = author.split(";|\\s+and\\s+");
        StringBuilder formatted = new StringBuilder();
        
        // Process each author and add appropriate separators
        for (int i = 0; i < authors.length; i++) {
            String name = authors[i].trim();
            if (name.isEmpty()) continue;
            
            // Format the individual author name
            formatted.append(formatSingleAuthorName(name));
            // Add the appropriate separator (comma, ampersand, or nothing)
            formatted.append(getAuthorSeparator(i, authors.length));
        }
        
        return formatted.toString().trim();
    }
    
    /**
     * Formats a single author name in APA style.
     * 
     * Determines the input format and delegates to the appropriate formatting method.
     * 
     * @param name A single author name to format
     * @return APA-formatted author name
     */
    private static String formatSingleAuthorName(String name) {
        // Check if name is already in "Last, First" format (contains comma)
        if (name.contains(",")) {
            return formatLastFirstName(name);
        } else {
            // Assume "First Middle Last" format
            return formatFirstLastName(name);
        }
    }
    
    /**
     * Formats a name already in "Last, First" format to APA style.
     * 
     * Converts "Last, First Middle" to "Last, F. M."
     * Extracts initials from the first name portion and adds proper formatting.
     * 
     * @param name Name in "Last, First" format
     * @return APA-formatted name with initials (e.g., "Smith, J. K.")
     */
    private static String formatLastFirstName(String name) {
        String[] parts = name.split(",", 2);
        String lastName = parts[0].trim();
        String firstName = parts[1].trim();
        
        StringBuilder result = new StringBuilder();
        result.append(lastName).append(", ");
        
        // Add initials from first name (handles middle names too)
        String[] firstParts = firstName.split("\\s+");
        for (String part : firstParts) {
            if (part.length() > 0) {
                result.append(part.charAt(0)).append(".");
                // Add space after initial
                if (part.length() == 1 || Character.isUpperCase(part.charAt(0))) {
                    result.append(" ");
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * Formats a name in "First Middle Last" format to APA style.
     * 
     * Converts "First Middle Last" to "Last, F. M."
     * The last word is treated as the surname, all others as given names.
     * 
     * @param name Name in "First Middle Last" format
     * @return APA-formatted name with initials, or original name if single word
     */
    private static String formatFirstLastName(String name) {
        String[] parts = name.split("\\s+");
        
        if (parts.length >= 2) {
            StringBuilder result = new StringBuilder();
            // Last name is the final part (surname comes last)
            result.append(parts[parts.length - 1]).append(", ");
            // Convert first/middle names to initials
            for (int j = 0; j < parts.length - 1; j++) {
                if (parts[j].length() > 0) {
                    result.append(parts[j].charAt(0)).append(". ");
                }
            }
            return result.toString();
        } else {
            // Single name (organization or mononym) - return as-is
            return name;
        }
    }
    
    /**
     * Returns the appropriate separator between authors based on position.
     * 
     * APA style uses:
     * - Commas between most authors
     * - Comma + ampersand (&) before the final author
     * - No separator after the last author
     * 
     * @param index Current author index (0-based)
     * @param totalAuthors Total number of authors
     * @return Appropriate separator string (", ", ", & ", or "")
     */
    private static String getAuthorSeparator(int index, int totalAuthors) {
        if (index < totalAuthors - 2) {
            // Not near the end: use comma
            return ", ";
        } else if (index == totalAuthors - 2) {
            // Second to last: use comma and ampersand
            return ", & ";
        }
        // Last author: no separator
        return "";
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