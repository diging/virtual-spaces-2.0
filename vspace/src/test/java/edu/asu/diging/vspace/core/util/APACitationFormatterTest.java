package edu.asu.diging.vspace.core.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.asu.diging.vspace.core.model.impl.Reference;

public class APACitationFormatterTest {
    
    private List<Reference> testReferences;
    
    @Before
    public void setUp() {
        testReferences = new ArrayList<>();
        
        // Create a journal article reference
        Reference journalRef = new Reference();
        journalRef.setTitle("Exploring Code Comprehension in Scientific Programming");
        journalRef.setAuthor("Alyssa Chen, Carol Wong");
        journalRef.setYear("2025");
        journalRef.setJournal("arXiv");
        journalRef.setUrl("http://arxiv.org/abs/2501.10037");
        journalRef.setType("Journal Article");
        testReferences.add(journalRef);
        
        // Create a book reference
        Reference bookRef = new Reference();
        bookRef.setTitle("Software Engineering Principles");
        bookRef.setAuthor("John Smith");
        bookRef.setYear("2020");
        bookRef.setVolume("2");
        bookRef.setPages("150-200");
        bookRef.setType("Book");
        testReferences.add(bookRef);
        
        // Create a thesis reference
        Reference thesisRef = new Reference();
        thesisRef.setTitle("Machine Learning Applications in Education");
        thesisRef.setAuthor("Jane Doe");
        thesisRef.setYear("2023");
        thesisRef.setJournal("University of Technology");
        thesisRef.setType("Thesis");
        testReferences.add(thesisRef);
    }
    
    @Test
    public void testFormatReferences_NotEmpty() {
        String result = APACitationFormatter.formatReferences(testReferences);
        
        assertNotNull("Result should not be null", result);
        assertFalse("Result should not be empty", result.isEmpty());
        assertTrue("Result should contain APA references div", result.contains("apa-references"));
    }
    
    @Test
    public void testFormatReferences_EmptyList() {
        List<Reference> emptyList = new ArrayList<>();
        String result = APACitationFormatter.formatReferences(emptyList);
        
        assertEquals("Empty list should return empty string", "", result);
    }
    
    @Test
    public void testFormatReferences_NullList() {
        String result = APACitationFormatter.formatReferences(null);
        
        assertEquals("Null list should return empty string", "", result);
    }
    
    @Test
    public void testJournalArticleFormatting() {
        String result = APACitationFormatter.formatReferences(testReferences);
        
        // Check for journal article specific formatting
        assertTrue("Should contain journal title in italics", result.contains("<em>arXiv</em>"));
        assertTrue("Should contain year in parentheses", result.contains("(2025)"));
        assertTrue("Should contain title in italics", result.contains("<em>Exploring Code Comprehension"));
        assertTrue("Should contain URL", result.contains("http://arxiv.org/abs/2501.10037"));
    }
    
    @Test
    public void testBookFormatting() {
        String result = APACitationFormatter.formatReferences(testReferences);
        
        // Check for book specific formatting
        assertTrue("Should contain book title in italics", result.contains("<em>Software Engineering Principles"));
        assertTrue("Should contain year in parentheses", result.contains("(2020)"));
        assertTrue("Should contain volume information", result.contains("Vol. 2"));
        assertTrue("Should contain pages information", result.contains("pp. 150-200"));
    }
    
    @Test
    public void testThesisFormatting() {
        String result = APACitationFormatter.formatReferences(testReferences);
        
        // Check for thesis specific formatting
        assertTrue("Should contain thesis title in italics", result.contains("<em>Machine Learning Applications"));
        assertTrue("Should contain year in parentheses", result.contains("(2023)"));
        assertTrue("Should contain 'Unpublished'", result.contains("Unpublished"));
        assertTrue("Should contain 'thesis'", result.contains("thesis"));
    }
    
    @Test
    public void testAuthorFormatting() {
        String result = APACitationFormatter.formatReferences(testReferences);
        
        // Check author formatting (Last name, First initial)
        assertTrue("Should format authors as 'Last, F.'", result.contains("Chen, A., Wong, C."));
        assertTrue("Should format single author as 'Last, F.'", result.contains("Smith, J."));
        assertTrue("Should format single author as 'Last, F.'", result.contains("Doe, J."));
    }
    
    @Test
    public void testHtmlStructure() {
        String result = APACitationFormatter.formatReferences(testReferences);
        
        // Check HTML structure
        assertTrue("Should contain proper div structure", result.contains("<div class=\"apa-references\">"));
        assertTrue("Should contain reference divs", result.contains("<div class=\"apa-reference\""));
        assertTrue("Should contain proper closing tags", result.contains("</div>"));
    }
}
