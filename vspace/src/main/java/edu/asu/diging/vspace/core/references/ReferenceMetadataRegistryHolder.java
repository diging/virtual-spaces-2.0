package edu.asu.diging.vspace.core.references;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Static holder for the IReferenceMetadataRegistry.
 *
 * This class allows JPA entities (like BiblioBlock) to access the registry
 * without requiring dependency injection, since entities cannot use @Autowired.
 *
 * The registry is set via Spring autowiring when this component is initialized.
 */
@Component
public class ReferenceMetadataRegistryHolder {

    private static IReferenceMetadataRegistry registry;

    @Autowired
    public void setRegistry(IReferenceMetadataRegistry registry) {
        ReferenceMetadataRegistryHolder.registry = registry;
    }

    public static IReferenceMetadataRegistry getRegistry() {
        return registry;
    }
}
