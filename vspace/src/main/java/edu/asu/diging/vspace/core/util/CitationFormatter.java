package edu.asu.diging.vspace.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CitationFormatter {
    
    // Pattern to match citation syntax: [@author year, pages]
    private static final Pattern CITATION_PATTERN = Pattern.compile(
        "\\[@([^,]+)\\s+(\\d{4})(?:,\\s*([^\\]]+))?\\]"
    );
    
    // Pattern to match full citation entries: {author, year, title, journal, etc.}
    private static final Pattern FULL_CITATION_PATTERN = Pattern.compile(
        "\\{([^}]+)\\}"
    );
    
    /**
     * Formats text containing citation markers into proper APA format.
     * 
     * @param text The input text containing citation markers
     * @return Formatted text with proper citations
     */
    public static String formatCitations(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\n");
        
        for (String line : lines) {
            result.append(formatLine(line)).append("\n");
        }
        
        return result.toString().trim();
    }
    
    /**
     * Formats a single line of text with citations.
     */
    private static String formatLine(String line) {
        // Handle full citation entries (transform to reference list format)
        line = formatFullCitations(line);
        
        // Handle in-text citations
        line = formatInTextCitations(line);
        
        return line;
    }
    
    /**
     * Formats in-text citations like [@author 2020] to (Author, 2020)
     */
    private static String formatInTextCitations(String text) {
        Matcher matcher = CITATION_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String author = matcher.group(1).trim();
            String year = matcher.group(2);
            String pages = matcher.group(3);
            
            // Capitalize first letter of author's last name
            author = capitalizeAuthor(author);
            
            String replacement;
            if (pages != null && !pages.trim().isEmpty()) {
                replacement = String.format("(%s, %s, %s)", author, year, pages.trim());
            } else {
                replacement = String.format("(%s, %s)", author, year);
            }
            
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * Formats full citation entries into APA reference format
     */
    private static String formatFullCitations(String text) {
        Matcher matcher = FULL_CITATION_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String citationData = matcher.group(1);
            String formattedReference = parseAndFormatReference(citationData);
            matcher.appendReplacement(result, formattedReference);
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * Parses citation data and formats it as APA reference
     */
    private static String parseAndFormatReference(String citationData) {
        String[] parts = citationData.split(",");
        if (parts.length < 3) {
            return citationData; // Return as-is if not enough parts
        }
        
        String author;
        String year;
        String title;
        int titleIndex;
        
        // Check if the first part looks like "LastName, FirstName" format
        if (parts.length >= 4 && parts[1].trim().matches("^[A-Z][a-z]*\\.?$|^[A-Z][a-z]+$")) {
            // Author is "LastName, FirstName" format (first two parts)
            author = parts[0].trim() + ", " + parts[1].trim();
            year = parts[2].trim();
            title = parts[3].trim();
            titleIndex = 4;
        } else {
            // Author is just the first part
            author = parts[0].trim();
            year = parts[1].trim();
            title = parts[2].trim();
            titleIndex = 3;
        }
        
        // Basic APA format: Author, A. (Year). Title. 
        StringBuilder reference = new StringBuilder();
        reference.append(capitalizeAuthor(author));
        reference.append(" (").append(year).append("). ");
        reference.append("*").append(title).append("*");
        
        if (parts.length > titleIndex) {
            String journal = parts[titleIndex].trim();
            reference.append(". ").append(journal);
        }
        
        if (parts.length > titleIndex + 1) {
            String pages = parts[titleIndex + 1].trim();
            reference.append(", ").append(pages);
        }
        
        reference.append(".");
        
        return reference.toString();
    }
    
    /**
     * Capitalizes author name properly for citations
     */
    private static String capitalizeAuthor(String author) {
        if (author == null || author.isEmpty()) {
            return author;
        }
        
        // Handle "et al." case - preserve it as is
        if (author.toLowerCase().contains("et al")) {
            return author; // Keep original formatting for et al.
        }
        
        // Handle "lastname, firstname" format
        if (author.contains(",")) {
            String[] nameParts = author.split(",");
            if (nameParts.length >= 2) {
                String lastName = nameParts[0].trim();
                String firstName = nameParts[1].trim();
                return capitalizeFirstLetter(lastName) + ", " + 
                       (firstName.length() > 0 ? Character.toUpperCase(firstName.charAt(0)) + "." : "");
            }
        }
        
        // Handle "firstname lastname" format
        String[] words = author.split("\\s+");
        if (words.length >= 2) {
            String firstName = words[0];
            String lastName = words[words.length - 1];
            return capitalizeFirstLetter(lastName) + ", " + 
                   (firstName.length() > 0 ? Character.toUpperCase(firstName.charAt(0)) + "." : "");
        }
        
        return capitalizeFirstLetter(author);
    }
    
    /**
     * Capitalizes the first letter of a string
     */
    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Validates if text contains properly formatted citations
     */
    public static boolean hasValidCitations(String text) {
        if (text == null || text.isEmpty()) {
            return true; // Empty text is valid
        }
        
        // Check for basic citation patterns
        return CITATION_PATTERN.matcher(text).find() || 
               FULL_CITATION_PATTERN.matcher(text).find() ||
               !containsUnformattedReferences(text);
    }
    
    /**
     * Checks if text contains unformatted references that should be citations
     */
    private static boolean containsUnformattedReferences(String text) {
        // Look for patterns that suggest unformatted references
        String lowerText = text.toLowerCase();
        return lowerText.contains("journal article") || 
               lowerText.contains("report") ||
               (lowerText.contains("20") && lowerText.matches(".*\\b\\d{4}\\b.*"));
    }
} 