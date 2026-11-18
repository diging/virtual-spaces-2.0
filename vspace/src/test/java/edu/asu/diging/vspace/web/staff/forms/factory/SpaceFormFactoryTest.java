package edu.asu.diging.vspace.web.staff.forms.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

public class SpaceFormFactoryTest {

    @Spy
    private LocalizedTextFormFactory localizedTextFormCreation;

    @Mock
    private IExhibitionManager exhibitionManager;

    @InjectMocks
    private SpaceFormFactory serviceToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createNewSpaceForm_noLanguagesConfigured() {
        Exhibition exhibition = new Exhibition();
        exhibition.setLanguages(new ArrayList<>());
        Space space = new Space();
        space.setName("Garden");
        space.setDescription("A quiet garden");

        SpaceForm form = serviceToTest.createNewSpaceForm(space, exhibition);

        assertEquals("Garden", form.getDefaultName().getText());
        assertEquals("A quiet garden", form.getDefaultDescription().getText());
        assertEquals(LocalizedTextFormFactory.DEFAULT_LABEL, form.getDefaultName().getExhibitionLanguageLabel());
        assertNull(form.getDefaultName().getExhibitionLanguageId());
        assertTrue(form.getNames().isEmpty());
    }

    @Test
    public void test_createNewSpaceForm_withDefaultLanguage() {
        Exhibition exhibition = new Exhibition();
        ExhibitionLanguage english = new ExhibitionLanguage("English", "en", exhibition);
        english.setId("EXL1");
        english.setDefault(true);
        ExhibitionLanguage spanish = new ExhibitionLanguage("Spanish", "es", exhibition);
        spanish.setId("EXL2");
        exhibition.getLanguages().add(english);
        exhibition.getLanguages().add(spanish);

        SpaceForm form = serviceToTest.createNewSpaceForm(new Space(), exhibition);

        assertEquals("EXL1", form.getDefaultName().getExhibitionLanguageId());
        assertEquals(1, form.getNames().size());
        assertEquals("EXL2", form.getNames().get(0).getExhibitionLanguageId());
    }

    @Test
    public void test_createNewSpaceForm_existingSpaceWithoutLocalizedTexts() {
        Exhibition exhibition = new Exhibition();
        ExhibitionLanguage english = new ExhibitionLanguage("English", "en", exhibition);
        english.setId("EXL1");
        english.setDefault(true);
        exhibition.getLanguages().add(english);
        Space space = new Space();
        space.setName("Garden");
        space.setDescription("A quiet garden");

        SpaceForm form = serviceToTest.createNewSpaceForm(space, exhibition);

        assertEquals("Garden", form.getDefaultName().getText());
        assertEquals("A quiet garden", form.getDefaultDescription().getText());
        assertEquals("EXL1", form.getDefaultName().getExhibitionLanguageId());
    }
}
