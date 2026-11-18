package edu.asu.diging.vspace.core.model.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.asu.diging.vspace.core.model.ILocalizedText;

public class SpaceTest {

    private Space space;

    @Before
    public void setUp() {
        ExhibitionLanguage english = new ExhibitionLanguage("English", "en", null);
        ExhibitionLanguage spanish = new ExhibitionLanguage("Spanish", "es", null);
        ExhibitionLanguage french = new ExhibitionLanguage("French", "fr", null);
        List<ILocalizedText> names = new ArrayList<>();
        names.add(new LocalizedText(english, "Garden"));
        names.add(new LocalizedText(spanish, "Jardín"));
        names.add(new LocalizedText(french, " "));
        space = new Space();
        space.setName("Garden");
        space.setSpaceNames(names);
    }

    @Test
    public void test_getNameForLanguage_translated() {
        assertEquals("Jardín", space.getNameForLanguage("es"));
    }

    @Test
    public void test_getNameForLanguage_noTranslationFallsBackToDefaultName() {
        assertEquals("Garden", space.getNameForLanguage("de"));
    }

    @Test
    public void test_getNameForLanguage_blankTranslationFallsBackToDefaultName() {
        assertEquals("Garden", space.getNameForLanguage("fr"));
    }

    @Test
    public void test_getNameForLanguage_nullCode() {
        assertEquals("Garden", space.getNameForLanguage(null));
    }
}
