package edu.asu.diging.vspace.core.references.impl;

import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.ReferenceDisplayFormatter;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

/**
 * APA citation style provider.
 *
 * This provider uses ReferenceDisplayFormatter to build citations in APA format.
 * It demonstrates how different formatters can call the display formatter
 * with different parameters (e.g., parentheses for year, italic for titles).
 */
@Component
public class APAReferenceMetadataProvider implements IReferenceMetadataProvider {

    @Override
    public ReferenceMetadataType getReferenceMetadataType() {
        return ReferenceMetadataType.APA;
    }

    @Override
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException {
        if (reference == null) {
            return "";
        }

        try {
            ReferenceDisplayFormatter formatter = new ReferenceDisplayFormatter();
            String type = (reference.getType() != null) ? reference.getType().toLowerCase() : "generic";

            // Author(s) - formatted in APA style
            String authors = formatAuthors(reference.getAuthor());
            formatter.addAuthors(authors);

            // Year - APA uses parentheses: "(year). "
            formatter.addYear(reference.getYear(), true);

            // Format based on reference type
            switch (type) {
                case "journal article":
                    formatJournalArticle(reference, formatter);
                    break;
                case "book":
                    formatBook(reference, formatter);
                    break;
                case "thesis":
                    formatThesis(reference, formatter);
                    break;
                case "patent":
                    formatPatent(reference, formatter);
                    break;
                case "report":
                    formatReport(reference, formatter);
                    break;
                case "website":
                    formatWebsite(reference, formatter);
                    break;
                default:
                    formatGeneric(reference, formatter);
                    break;
            }

            // URL (if not already included in type-specific formatting)
            if (reference.getUrl() != null && !reference.getUrl().isEmpty()
                    && !type.equals("website")) {
                formatter.addUrlWithPrefix(reference.getUrl(), " Retrieved from ");
            }

            return formatter.getReferenceDisplayText();
        } catch (Exception e) {
            throw new ReferenceMetadataEncodingException("Error encoding APA reference", e);
        }
    }

    /**
     * Formats author names in APA style.
     */
    private String formatAuthors(String author) {
        if (author == null || author.isEmpty()) {
            return "";
        }

        // Split by semicolon or " and " to handle multiple authors
        String[] authors = author.split(";|\\s+and\\s+");
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < authors.length; i++) {
            String name = authors[i].trim();
            if (name.isEmpty()) {
                continue;
            }

            formatted.append(formatSingleAuthorName(name));
            formatted.append(getAuthorSeparator(i, authors.length));
        }

        return formatted.toString().trim();
    }

    private String formatSingleAuthorName(String name) {
        if (name.contains(",")) {
            return formatLastFirstName(name);
        } else {
            return formatFirstLastName(name);
        }
    }

    private String formatLastFirstName(String name) {
        String[] parts = name.split(",", 2);
        String lastName = parts[0].trim();
        String firstName = parts[1].trim();

        StringBuilder result = new StringBuilder();
        result.append(lastName).append(", ");

        String[] firstParts = firstName.split("\\s+");
        for (String part : firstParts) {
            if (part.length() > 0) {
                result.append(part.charAt(0)).append(".");
                if (part.length() == 1 || Character.isUpperCase(part.charAt(0))) {
                    result.append(" ");
                }
            }
        }

        return result.toString();
    }

    private String formatFirstLastName(String name) {
        String[] parts = name.split("\\s+");

        if (parts.length >= 2) {
            StringBuilder result = new StringBuilder();
            result.append(parts[parts.length - 1]).append(", ");
            for (int j = 0; j < parts.length - 1; j++) {
                if (parts[j].length() > 0) {
                    result.append(parts[j].charAt(0)).append(". ");
                }
            }
            return result.toString();
        } else {
            return name;
        }
    }

    private String getAuthorSeparator(int index, int totalAuthors) {
        if (index < totalAuthors - 2) {
            return ", ";
        } else if (index == totalAuthors - 2) {
            return ", & ";
        }
        return "";
    }

    /**
     * Formats journal article references using ReferenceDisplayFormatter.
     * APA format: Article title. Journal Name, volume(issue), pages.
     */
    private void formatJournalArticle(Reference ref, ReferenceDisplayFormatter formatter) {
        // Article title (NOT italicized for journal articles)
        formatter.addTitle(ref.getTitle());

        // Journal name (italicized)
        formatter.addJournalItalic(ref.getJournal());

        // Volume (italicized) - add comma before if journal present
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            formatter.addRawText(", ");
            formatter.addVolumeItalic(ref.getVolume());
        }

        // Issue (in parentheses, not italicized)
        formatter.addIssue(ref.getIssue());

        // Pages (no prefix for journal articles in APA)
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            formatter.addRawText(", ");
            formatter.addPages(ref.getPages(), "");
        }

        formatter.addRawText(".");
    }

    /**
     * Formats book references using ReferenceDisplayFormatter.
     * APA format: Book title (Vol. X, pp. Y-Z). Publisher.
     */
    private void formatBook(Reference ref, ReferenceDisplayFormatter formatter) {
        // Book title (italicized)
        formatter.addTitleItalic(ref.getTitle());

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
            formatter.addRawText(" (").addRawText(extras.toString()).addRawText(")");
        }

        formatter.addRawText(".");

        // Editors
        formatter.addEditors(ref.getEditors());

        // Publisher (from journal field)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            formatter.addRawText(" ").addRawText(ref.getJournal()).addRawText(".");
        }
    }

    /**
     * Formats thesis references using ReferenceDisplayFormatter.
     * APA format: Title (Unpublished doctoral/master's dissertation). Institution.
     */
    private void formatThesis(Reference ref, ReferenceDisplayFormatter formatter) {
        // Title (italicized)
        formatter.addTitleItalic(ref.getTitle());

        formatter.addRawText(" (Unpublished");
        if (ref.getNote() != null && !ref.getNote().isEmpty()) {
            formatter.addRawText(" ").addNote(ref.getNote());
        } else {
            formatter.addRawText(" doctoral dissertation");
        }
        formatter.addRawText(").");

        // Institution (from journal field)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            formatter.addRawText(" ").addRawText(ref.getJournal()).addRawText(".");
        }
    }

    /**
     * Formats patent references using ReferenceDisplayFormatter.
     * APA format: Title (U.S. Patent No. XXXXX).
     */
    private void formatPatent(Reference ref, ReferenceDisplayFormatter formatter) {
        // Title (italicized)
        formatter.addTitleItalic(ref.getTitle());

        formatter.addRawText(" (U.S. Patent No. ");
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            formatter.addRawText(ref.getVolume());
        } else if (ref.getIssue() != null && !ref.getIssue().isEmpty()) {
            formatter.addRawText(ref.getIssue());
        }
        formatter.addRawText(").");
    }

    /**
     * Formats report references using ReferenceDisplayFormatter.
     * APA format: Title (Report No. XXX). Publisher.
     */
    private void formatReport(Reference ref, ReferenceDisplayFormatter formatter) {
        // Title (italicized)
        formatter.addTitleItalic(ref.getTitle());

        // Report number
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            formatter.addRawText(" (Report No. ").addRawText(ref.getVolume()).addRawText(")");
        }

        formatter.addRawText(".");

        // Publisher (from journal field)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            formatter.addRawText(" ").addRawText(ref.getJournal()).addRawText(".");
        }
    }

    /**
     * Formats website references using ReferenceDisplayFormatter.
     * APA format: Title. Website Name. URL
     */
    private void formatWebsite(Reference ref, ReferenceDisplayFormatter formatter) {
        // Title (NOT italicized for web pages)
        formatter.addTitle(ref.getTitle());

        // Website name (italicized)
        if (ref.getJournal() != null && !ref.getJournal().isEmpty()) {
            formatter.addJournalItalic(ref.getJournal());
            formatter.addRawText(".");
        }

        // URL (always included for websites)
        formatter.addUrlWithPrefix(ref.getUrl(), " Retrieved from ");
    }

    /**
     * Formats generic references using ReferenceDisplayFormatter.
     * Fallback for unknown types.
     */
    private void formatGeneric(Reference ref, ReferenceDisplayFormatter formatter) {
        // Title
        formatter.addTitle(ref.getTitle());

        // Source/Journal (italicized)
        formatter.addJournalItalic(ref.getJournal());

        // Volume
        if (ref.getVolume() != null && !ref.getVolume().isEmpty()) {
            formatter.addRawText(", ").addRawText(ref.getVolume());
        }

        // Issue
        formatter.addIssue(ref.getIssue());

        // Pages
        if (ref.getPages() != null && !ref.getPages().isEmpty()) {
            formatter.addRawText(", ").addPages(ref.getPages(), "");
        }

        formatter.addRawText(".");
    }
}
