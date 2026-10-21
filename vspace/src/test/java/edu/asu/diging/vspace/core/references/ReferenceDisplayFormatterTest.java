package edu.asu.diging.vspace.core.references;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ReferenceDisplayFormatterTest {

    private ReferenceDisplayFormatter formatter;

    @Before
    public void setUp() {
        formatter = new ReferenceDisplayFormatter();
    }

    @Test
    public void testAddAuthors() {
        String result = formatter.addAuthors("Smith, J.").getReferenceDisplayText();
        assertEquals("Smith, J. ", result);
    }

    @Test
    public void testAddAuthors_WithoutPeriod() {
        String result = formatter.addAuthors("Smith, J").getReferenceDisplayText();
        assertEquals("Smith, J. ", result);
    }

    @Test
    public void testAddAuthors_Null() {
        String result = formatter.addAuthors(null).getReferenceDisplayText();
        assertEquals("", result);
    }

    @Test
    public void testAddYear_WithParentheses() {
        String result = formatter.addYear("2024", true).getReferenceDisplayText();
        assertEquals("(2024). ", result);
    }

    @Test
    public void testAddYear_WithoutParentheses() {
        String result = formatter.addYear("2024", false).getReferenceDisplayText();
        assertEquals("2024. ", result);
    }

    @Test
    public void testAddYear_Default() {
        String result = formatter.addYear("2024").getReferenceDisplayText();
        assertEquals("(2024). ", result);
    }

    @Test
    public void testAddTitle() {
        String result = formatter.addTitle("Test Title").getReferenceDisplayText();
        assertEquals("Test Title. ", result);
    }

    @Test
    public void testAddTitle_WithPeriod() {
        String result = formatter.addTitle("Test Title.").getReferenceDisplayText();
        assertEquals("Test Title. ", result);
    }

    @Test
    public void testAddTitleItalic() {
        String result = formatter.addTitleItalic("Test Title").getReferenceDisplayText();
        assertEquals("<em>Test Title</em>", result);
    }

    @Test
    public void testAddJournal() {
        String result = formatter.addJournal("Nature").getReferenceDisplayText();
        assertEquals("In: Nature. ", result);
    }

    @Test
    public void testAddJournalItalic() {
        String result = formatter.addJournalItalic("Nature").getReferenceDisplayText();
        assertEquals("<em>Nature</em>", result);
    }

    @Test
    public void testAddPages_DefaultPrefix() {
        String result = formatter.addPages("100-200").getReferenceDisplayText();
        assertEquals("p. 100-200", result);
    }

    @Test
    public void testAddPages_CustomPrefix() {
        String result = formatter.addPages("100-200", "pp. ").getReferenceDisplayText();
        assertEquals("pp. 100-200", result);
    }

    @Test
    public void testAddPages_NoPrefix() {
        String result = formatter.addPages("100-200", "").getReferenceDisplayText();
        assertEquals("100-200", result);
    }

    @Test
    public void testAddVolume() {
        String result = formatter.addVolume("5").getReferenceDisplayText();
        assertEquals("5. ", result);
    }

    @Test
    public void testAddVolumeItalic() {
        String result = formatter.addVolumeItalic("5").getReferenceDisplayText();
        assertEquals("<em>5</em>", result);
    }

    @Test
    public void testAddIssue() {
        String result = formatter.addIssue("3").getReferenceDisplayText();
        assertEquals("(3)", result);
    }

    @Test
    public void testAddUrl() {
        String result = formatter.addUrl("https://example.com").getReferenceDisplayText();
        assertEquals("https://example.com", result);
    }

    @Test
    public void testAddUrlWithPrefix() {
        String result = formatter.addUrlWithPrefix("https://example.com", "Retrieved from ").getReferenceDisplayText();
        assertEquals("Retrieved from https://example.com", result);
    }

    @Test
    public void testAddEditors() {
        String result = formatter.addEditors("John Smith").getReferenceDisplayText();
        assertEquals(" John Smith (Ed.).", result);
    }

    @Test
    public void testAddNote() {
        String result = formatter.addNote("master's thesis").getReferenceDisplayText();
        assertEquals("master's thesis", result);
    }

    @Test
    public void testAddRawText() {
        String result = formatter.addRawText(", ").getReferenceDisplayText();
        assertEquals(", ", result);
    }

    @Test
    public void testClear() {
        formatter.addAuthors("Smith, J.").addYear("2024");
        formatter.clear();
        String result = formatter.getReferenceDisplayText();
        assertEquals("", result);
    }

    @Test
    public void testChainedCalls() {
        String result = formatter
            .addAuthors("Smith, J.")
            .addYear("2024", true)
            .addTitle("Test Article")
            .addJournalItalic("Nature")
            .addRawText(", ")
            .addVolumeItalic("5")
            .addIssue("3")
            .addRawText(", ")
            .addPages("100-200", "")
            .addRawText(".")
            .getReferenceDisplayText();

        assertEquals("Smith, J. (2024). Test Article. <em>Nature</em>, <em>5</em>(3), 100-200.", result);
    }

    @Test
    public void testNullAndEmptyValues_Ignored() {
        String result = formatter
            .addAuthors(null)
            .addYear("")
            .addTitle(null)
            .addJournalItalic("")
            .addPages(null, "p. ")
            .getReferenceDisplayText();

        assertEquals("", result);
    }
}
