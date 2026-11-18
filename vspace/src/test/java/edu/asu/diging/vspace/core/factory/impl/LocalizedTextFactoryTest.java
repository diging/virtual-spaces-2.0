package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

public class LocalizedTextFactoryTest {

    @Mock
    private LocalizedTextRepository localizedTextRepo;

    @Mock
    private ExhibitionLanguageRepository exhibitionLanguageRepository;

    @InjectMocks
    private LocalizedTextFactory serviceToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createLocalizedText_noLanguage_returnsNullWithoutLookups() {
        LocalizedTextForm form = new LocalizedTextForm("Garden", null, null, "Default");

        assertNull(serviceToTest.createLocalizedText(null, form, new ArrayList<>()));
        verify(localizedTextRepo, never()).findById(any());
        verify(exhibitionLanguageRepository, never()).findById(any());
    }

    @Test
    public void test_createLocalizedText_emptyText_returnsNull() {
        LocalizedTextForm form = new LocalizedTextForm("", "", "EXL1", "English");

        assertNull(serviceToTest.createLocalizedText(null, form, new ArrayList<>()));
    }

    @Test
    public void test_createLocalizedText_newText() {
        ExhibitionLanguage spanish = new ExhibitionLanguage("Spanish", "es", null);
        when(exhibitionLanguageRepository.findById("EXL2")).thenReturn(Optional.of(spanish));
        LocalizedTextForm form = new LocalizedTextForm("Jardín", "", "EXL2", "Spanish");

        LocalizedText text = serviceToTest.createLocalizedText(null, form, new ArrayList<>());

        assertEquals("Jardín", text.getText());
        assertEquals(spanish, text.getExhibitionLanguage());
        verify(localizedTextRepo, never()).findById(any());
    }

    @Test
    public void test_createLocalizedText_existingTextIsUpdated() {
        LocalizedText existing = new LocalizedText(new ExhibitionLanguage("Spanish", "es", null), "Old");
        when(localizedTextRepo.findById("LOC1")).thenReturn(Optional.of(existing));
        LocalizedTextForm form = new LocalizedTextForm("Nuevo", "LOC1", "EXL2", "Spanish");

        LocalizedText text = serviceToTest.createLocalizedText(null, form, new ArrayList<>());

        assertEquals(existing, text);
        assertEquals("Nuevo", existing.getText());
    }
}
