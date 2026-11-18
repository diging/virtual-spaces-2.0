package edu.asu.diging.vspace.core.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExhibitionAboutPageTest {

    private ExhibitionAboutPage aboutPage;

    @Before
    public void setUp() {
        ExhibitionLanguage english = new ExhibitionLanguage("English", "en", null);
        ExhibitionLanguage spanish = new ExhibitionLanguage("Spanish", "es", null);
        aboutPage = new ExhibitionAboutPage();
        aboutPage.setTitle("About us");
        aboutPage.setAboutPageText("Welcome");
        aboutPage.getExhibitionTitles().add(new LocalizedText(english, "About us"));
        aboutPage.getExhibitionTitles().add(new LocalizedText(spanish, "Sobre nosotros"));
        aboutPage.getExhibitionTextDescriptions().add(new LocalizedText(spanish, "**Bienvenidos**"));
    }

    @Test
    public void test_getTitleForLanguage_translated() {
        assertEquals("Sobre nosotros", aboutPage.getTitleForLanguage("es"));
    }

    @Test
    public void test_getTitleForLanguage_fallsBackToDefault() {
        assertEquals("About us", aboutPage.getTitleForLanguage("fr"));
    }

    @Test
    public void test_htmlRenderedText_translated() {
        assertEquals("<p><strong>Bienvenidos</strong></p>\n", aboutPage.htmlRenderedText("es"));
    }

    @Test
    public void test_htmlRenderedText_fallsBackToDefault() {
        assertEquals("<p>Welcome</p>\n", aboutPage.htmlRenderedText("en"));
    }

    @Test
    public void test_htmlRenderedText_noText() {
        assertEquals("", new ExhibitionAboutPage().htmlRenderedText());
    }
}
