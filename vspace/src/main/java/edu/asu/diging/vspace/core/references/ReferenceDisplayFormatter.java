package edu.asu.diging.vspace.core.references;

/**
 * Builder class for formatting reference display text.
 *
 * This class provides a fluent API to construct formatted reference strings.
 * Different citation style providers (e.g., APA, MLA) can call these methods
 * in different orders with different parameters to achieve style-specific formatting.
 */
public class ReferenceDisplayFormatter {

    private StringBuilder referenceDisplayText;

    public ReferenceDisplayFormatter() {
        referenceDisplayText = new StringBuilder();
    }

    public String getReferenceDisplayText() {
        return referenceDisplayText.toString();
    }

    /**
     * Clears the current content and resets the formatter for reuse.
     */
    public ReferenceDisplayFormatter clear() {
        referenceDisplayText = new StringBuilder();
        return this;
    }

    /**
     * Adds raw text without any formatting or modification.
     */
    public ReferenceDisplayFormatter addRawText(String text) {
        if (text != null && !text.isEmpty()) {
            this.referenceDisplayText.append(text);
        }
        return this;
    }

    public ReferenceDisplayFormatter addAuthors(String author) {
        if (author != null && !author.isEmpty()) {
            this.referenceDisplayText.append(author);
            if (!author.endsWith(".")) {
                this.referenceDisplayText.append(".");
            }
            this.referenceDisplayText.append(" ");
        }
        return this;
    }

    /**
     * Adds year with default parentheses formatting: "(year). "
     */
    public ReferenceDisplayFormatter addYear(String year) {
        return addYear(year, true);
    }

    /**
     * Adds year with configurable parentheses.
     *
     * @param year the year string
     * @param withParentheses if true, formats as "(year). "; if false, formats as "year. "
     */
    public ReferenceDisplayFormatter addYear(String year, boolean withParentheses) {
        if (year != null && !year.isEmpty()) {
            if (withParentheses) {
                this.referenceDisplayText.append("(").append(year).append("). ");
            } else {
                this.referenceDisplayText.append(year).append(". ");
            }
        }
        return this;
    }

    /**
     * Adds title without italic formatting (for journal articles, web pages).
     */
    public ReferenceDisplayFormatter addTitle(String title) {
        if (title != null && !title.isEmpty()) {
            String trimmedTitle = title.trim();
            this.referenceDisplayText.append(trimmedTitle);
            if (!trimmedTitle.endsWith(".")) {
                this.referenceDisplayText.append(".");
            }
            this.referenceDisplayText.append(" ");
        }
        return this;
    }

    /**
     * Adds title with HTML italic formatting (for books, theses).
     */
    public ReferenceDisplayFormatter addTitleItalic(String title) {
        if (title != null && !title.isEmpty()) {
            String trimmedTitle = title.trim();
            this.referenceDisplayText.append("<em>").append(trimmedTitle).append("</em>");
        }
        return this;
    }

    /**
     * Adds journal with "In: " prefix (original behavior).
     */
    public ReferenceDisplayFormatter addJournal(String journal) {
        if (journal != null && !journal.isEmpty()) {
            this.referenceDisplayText.append("In: ").append(journal).append(". ");
        }
        return this;
    }

    /**
     * Adds journal with HTML italic formatting (for APA style).
     */
    public ReferenceDisplayFormatter addJournalItalic(String journal) {
        if (journal != null && !journal.isEmpty()) {
            this.referenceDisplayText.append("<em>").append(journal).append("</em>");
        }
        return this;
    }

    /**
     * Adds pages with default "p. " prefix.
     */
    public ReferenceDisplayFormatter addPages(String pages) {
        return addPages(pages, "p. ");
    }

    /**
     * Adds pages with configurable prefix.
     *
     * @param pages the pages string
     * @param prefix the prefix (e.g., "p. ", "pp. ", or empty string for no prefix)
     */
    public ReferenceDisplayFormatter addPages(String pages, String prefix) {
        if (pages != null && !pages.isEmpty()) {
            if (prefix != null && !prefix.isEmpty()) {
                this.referenceDisplayText.append(prefix);
            }
            this.referenceDisplayText.append(pages);
        }
        return this;
    }

    /**
     * Adds volume without formatting.
     */
    public ReferenceDisplayFormatter addVolume(String volume) {
        if (volume != null && !volume.isEmpty()) {
            this.referenceDisplayText.append(volume).append(". ");
        }
        return this;
    }

    /**
     * Adds volume with HTML italic formatting (for APA journal articles).
     */
    public ReferenceDisplayFormatter addVolumeItalic(String volume) {
        if (volume != null && !volume.isEmpty()) {
            this.referenceDisplayText.append("<em>").append(volume).append("</em>");
        }
        return this;
    }

    public ReferenceDisplayFormatter addIssue(String issue) {
        if (issue != null && !issue.isEmpty()) {
            this.referenceDisplayText.append("(").append(issue).append(")");
        }
        return this;
    }

    public ReferenceDisplayFormatter addUrl(String url) {
        if (url != null && !url.isEmpty()) {
            this.referenceDisplayText.append(url);
        }
        return this;
    }

    /**
     * Adds URL with "Retrieved from " prefix (for APA style).
     */
    public ReferenceDisplayFormatter addUrlWithPrefix(String url, String prefix) {
        if (url != null && !url.isEmpty()) {
            if (prefix != null && !prefix.isEmpty()) {
                this.referenceDisplayText.append(prefix);
            }
            this.referenceDisplayText.append(url);
        }
        return this;
    }

    /**
     * Adds editors with "(Ed.)" suffix.
     */
    public ReferenceDisplayFormatter addEditors(String editors) {
        if (editors != null && !editors.isEmpty()) {
            this.referenceDisplayText.append(" ").append(editors).append(" (Ed.).");
        }
        return this;
    }

    /**
     * Adds a note field (for thesis type, etc.).
     */
    public ReferenceDisplayFormatter addNote(String note) {
        if (note != null && !note.isEmpty()) {
            this.referenceDisplayText.append(note);
        }
        return this;
    }

}