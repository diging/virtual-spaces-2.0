package edu.asu.diging.vspace.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Test class for CitationFormatter utility
 */
public class CitationFormatterTest {

    @Test
    public void testFormatInTextCitation() {
        String input = "This research shows [@Smith 2020] that citations work.";
        String expected = "This research shows (Smith, 2020) that citations work.";
        String result = CitationFormatter.formatCitations(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatInTextCitationWithPages() {
        String input = "As noted [@Jones 2019, pp. 15-20], proper formatting is important.";
        String expected = "As noted (Jones, 2019, pp. 15-20), proper formatting is important.";
        String result = CitationFormatter.formatCitations(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatFullCitation() {
        String input = "{Smith, J., 2020, Research Methods, Journal of Science, pp. 1-10}";
        String expected = "Smith, J. (2020). *Research Methods*. Journal of Science, pp. 1-10.";
        String result = CitationFormatter.formatCitations(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatMultipleInTextCitations() {
        String input = "Research by [@Smith 2020] and [@Jones 2019] shows this.";
        String expected = "Research by (Smith, 2020) and (Jones, 2019) shows this.";
        String result = CitationFormatter.formatCitations(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMixedCitations() {
        String input = "This study [@Chen 2025] examines the issue.\n\n" +
                      "{Chen, A., 2025, Code Comprehension in Scientific Programming, arXiv, http://arxiv.org/abs/2501.10037}";
        String result = CitationFormatter.formatCitations(input);
        
        assertTrue("Should contain formatted in-text citation", result.contains("(Chen, 2025)"));
        assertTrue("Should contain formatted reference", result.contains("Chen, A. (2025)"));
        assertTrue("Should contain italicized title", result.contains("*Code Comprehension in Scientific Programming*"));
    }

    @Test
    public void testAuthorNameFormatting() {
        String input = "[@John Smith 2020]";
        String expected = "(Smith, J., 2020)";
        String result = CitationFormatter.formatCitations(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEtAlCitation() {
        String input = "[@Smith et al. 2020]";
        String expected = "(Smith et al., 2020)";
        String result = CitationFormatter.formatCitations(input);
        assertEquals(expected, result);
    }

    @Test
    public void testValidCitationsDetection() {
        String validText1 = "This has [@Smith 2020] proper citations.";
        String validText2 = "Reference: {Smith, J., 2020, Title, Journal}";
        String validText3 = "No citations but valid text.";
        String invalidText = "This has 2020 journal article but no proper format.";

        assertTrue("Should recognize valid in-text citation", 
                  CitationFormatter.hasValidCitations(validText1));
        assertTrue("Should recognize valid full citation", 
                  CitationFormatter.hasValidCitations(validText2));
        assertTrue("Should accept text without citations", 
                  CitationFormatter.hasValidCitations(validText3));
        assertFalse("Should detect improperly formatted references", 
                   CitationFormatter.hasValidCitations(invalidText));
    }

    @Test
    public void testEmptyAndNullInput() {
        assertEquals("Should handle null input", null, CitationFormatter.formatCitations(null));
        assertEquals("Should handle empty input", "", CitationFormatter.formatCitations(""));
        assertTrue("Should validate null as valid", CitationFormatter.hasValidCitations(null));
        assertTrue("Should validate empty as valid", CitationFormatter.hasValidCitations(""));
    }



    @Test
    public void testComplexReference() {
        String input = "{Chen, Alyssa, 2025, Exploring Code Comprehension in Scientific Programming: Preliminary Insights from Research Scientists, arXiv, http://arxiv.org/abs/2501.10037}";
        String result = CitationFormatter.formatCitations(input);
        
        assertTrue("Should format author correctly", result.contains("Chen, A."));
        assertTrue("Should include year", result.contains("(2025)"));
        assertTrue("Should italicize title", result.contains("*Exploring Code Comprehension"));
        assertTrue("Should include journal", result.contains("arXiv"));
        assertTrue("Should include URL", result.contains("http://arxiv.org/abs/2501.10037"));
    }

    @Test
    public void testPreserveNonCitationText() {
        String input = "Regular text with no citations should remain unchanged.";
        String result = CitationFormatter.formatCitations(input);
        assertEquals("Non-citation text should be preserved", input, result);
    }
} 