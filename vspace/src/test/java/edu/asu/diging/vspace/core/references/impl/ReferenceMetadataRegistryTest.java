package edu.asu.diging.vspace.core.references.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

public class ReferenceMetadataRegistryTest {

    @InjectMocks
    private ReferenceMetadataRegistry registry;

    @Mock
    private IReferenceMetadataProvider apaProvider;

    @Mock
    private IReferenceMetadataProvider defaultProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(apaProvider.getReferenceMetadataType()).thenReturn(ReferenceMetadataType.APA);
        when(defaultProvider.getReferenceMetadataType()).thenReturn(ReferenceMetadataType.DEFAULT);

        List<IReferenceMetadataProvider> providers = new ArrayList<>();
        providers.add(apaProvider);
        providers.add(defaultProvider);

        ReflectionTestUtils.setField(registry, "providers", providers);
        registry.init();
    }

    @Test
    public void testGetProvider_ByType_APA() {
        IReferenceMetadataProvider result = registry.getProvider(ReferenceMetadataType.APA);
        assertEquals(apaProvider, result);
    }

    @Test
    public void testGetProvider_ByType_DEFAULT() {
        IReferenceMetadataProvider result = registry.getProvider(ReferenceMetadataType.DEFAULT);
        assertEquals(defaultProvider, result);
    }

    @Test
    public void testGetProvider_ByString_APA() {
        IReferenceMetadataProvider result = registry.getProvider("APA");
        assertEquals(apaProvider, result);
    }

    @Test
    public void testGetProvider_ByString_LowerCase() {
        IReferenceMetadataProvider result = registry.getProvider("apa");
        assertEquals(apaProvider, result);
    }

    @Test
    public void testGetProvider_ByString_Invalid_ReturnDefault() {
        IReferenceMetadataProvider result = registry.getProvider("INVALID");
        assertEquals(defaultProvider, result);
    }

    @Test
    public void testGetProvider_ByString_Null_ReturnDefault() {
        IReferenceMetadataProvider result = registry.getProvider((String) null);
        assertEquals(defaultProvider, result);
    }

    @Test
    public void testGetProvider_ByString_Empty_ReturnDefault() {
        IReferenceMetadataProvider result = registry.getProvider("");
        assertEquals(defaultProvider, result);
    }

    @Test
    public void testInit_WithNullProviders() {
        ReferenceMetadataRegistry emptyRegistry = new ReferenceMetadataRegistry();
        ReflectionTestUtils.setField(emptyRegistry, "providers", null);

        // Should not throw exception
        emptyRegistry.init();

        // Should return null for any type
        assertNull(emptyRegistry.getProvider(ReferenceMetadataType.APA));
    }
}
