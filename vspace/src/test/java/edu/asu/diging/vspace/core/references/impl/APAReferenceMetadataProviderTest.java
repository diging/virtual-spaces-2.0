package edu.asu.diging.vspace.core.references.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

public class APAReferenceMetadataProviderTest {

    private APAReferenceMetadataProvider provider;

    @Before
    public void setUp() {
        provider = new APAReferenceMetadataProvider();
    }

    @Test
    public void testGetReferenceMetadataType() {
        assertEquals(ReferenceMetadataType.APA, provider.getReferenceMetadataType());
    }

    @Test
    public void testGetReferenceMetadata_NullReference() throws ReferenceMetadataEncodingException {
        String result = provider.getReferenceMetadata(null);
        assertEquals("", result);
    }

    @Test
    public void testJournalArticleFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Exploring Code Comprehension in Scientific Programming");
        ref.setAuthor("Alyssa Chen, Carol Wong");
        ref.setYear("2025");
        ref.setJournal("arXiv");
        ref.setUrl("http://arxiv.org/abs/2501.10037");
        ref.setType("Journal Article");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain journal title in italics", result.contains("<em>arXiv</em>"));
        assertTrue("Should contain year in parentheses", result.contains("(2025)"));
        assertTrue("Should contain URL", result.contains("http://arxiv.org/abs/2501.10037"));
    }

    @Test
    public void testBookFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Software Engineering Principles");
        ref.setAuthor("John Smith");
        ref.setYear("2020");
        ref.setVolume("2");
        ref.setPages("150-200");
        ref.setType("Book");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain book title in italics", result.contains("<em>Software Engineering Principles</em>"));
        assertTrue("Should contain year in parentheses", result.contains("(2020)"));
        assertTrue("Should contain volume information", result.contains("Vol. 2"));
        assertTrue("Should contain pages information", result.contains("pp. 150-200"));
    }

    @Test
    public void testThesisFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Machine Learning Applications in Education");
        ref.setAuthor("Jane Doe");
        ref.setYear("2023");
        ref.setJournal("University of Technology");
        ref.setType("Thesis");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain thesis title in italics", result.contains("<em>Machine Learning Applications"));
        assertTrue("Should contain year in parentheses", result.contains("(2023)"));
        assertTrue("Should contain 'Unpublished'", result.contains("Unpublished"));
    }

    @Test
    public void testAuthorFormatting_MultipleAuthors() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Test Article");
        ref.setAuthor("Alyssa Chen, Carol Wong");
        ref.setYear("2025");
        ref.setType("Journal Article");

        String result = provider.getReferenceMetadata(ref);

        // Check author formatting (Last name, First initial with ampersand for multiple authors)
        assertTrue("Should format authors with ampersand", result.contains(", &"));
    }

    @Test
    public void testAuthorFormatting_SingleAuthor() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Test Article");
        ref.setAuthor("John Smith");
        ref.setYear("2025");
        ref.setType("Journal Article");

        String result = provider.getReferenceMetadata(ref);

        assertTrue("Should format single author as 'Last, F.'", result.contains("Smith, J."));
    }

    @Test
    public void testPatentFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Innovative Technology Patent");
        ref.setAuthor("Jane Inventor");
        ref.setYear("2024");
        ref.setVolume("12345678");
        ref.setType("Patent");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain patent title in italics", result.contains("<em>Innovative Technology Patent</em>"));
        assertTrue("Should contain 'U.S. Patent No.'", result.contains("U.S. Patent No."));
        assertTrue("Should contain patent number", result.contains("12345678"));
    }

    @Test
    public void testWebsiteFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("How to Use Our API");
        ref.setAuthor("Documentation Team");
        ref.setYear("2024");
        ref.setJournal("Example Website");
        ref.setUrl("https://example.com/api-docs");
        ref.setType("Website");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain title (not italicized for website)", result.contains("How to Use Our API."));
        assertTrue("Should contain website name in italics", result.contains("<em>Example Website</em>"));
        assertTrue("Should contain 'Retrieved from'", result.contains("Retrieved from"));
        assertTrue("Should contain URL", result.contains("https://example.com/api-docs"));
    }

    @Test
    public void testReportFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Annual Research Report");
        ref.setAuthor("Research Department");
        ref.setYear("2024");
        ref.setVolume("2024-001");
        ref.setJournal("Research Institute");
        ref.setType("Report");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain report title in italics", result.contains("<em>Annual Research Report</em>"));
        assertTrue("Should contain 'Report No.'", result.contains("Report No."));
        assertTrue("Should contain report number", result.contains("2024-001"));
    }

    @Test
    public void testGenericFormatting() throws ReferenceMetadataEncodingException {
        Reference ref = new Reference();
        ref.setTitle("Some Generic Reference");
        ref.setAuthor("Unknown Author");
        ref.setYear("2024");
        ref.setJournal("Some Source");
        ref.setType("Other");

        String result = provider.getReferenceMetadata(ref);

        assertNotNull(result);
        assertTrue("Should contain title", result.contains("Some Generic Reference."));
        assertTrue("Should contain year", result.contains("(2024)"));
    }
}
