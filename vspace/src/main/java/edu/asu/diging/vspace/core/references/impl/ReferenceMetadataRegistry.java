package edu.asu.diging.vspace.core.references.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

/**
 * Registry implementation for managing reference metadata providers.
 *
 * This service automatically discovers all IReferenceMetadataProvider implementations
 * via Spring autowiring and registers them by their metadata type for lookup.
 */
@Service
public class ReferenceMetadataRegistry implements IReferenceMetadataRegistry {

    @Autowired
    private List<IReferenceMetadataProvider> providers;

    private Map<ReferenceMetadataType, IReferenceMetadataProvider> providerMap;

    @Override
    @PostConstruct
    public void init() {
        providerMap = new HashMap<>();
        if (providers != null) {
            for (IReferenceMetadataProvider provider : providers) {
                providerMap.put(provider.getReferenceMetadataType(), provider);
            }
        }
    }

    @Override
    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType) {
        return providerMap.get(refMetaDataType);
    }

    @Override
    public IReferenceMetadataProvider getProvider(String metadataType) {
        if (metadataType == null || metadataType.isEmpty()) {
            return providerMap.get(ReferenceMetadataType.DEFAULT);
        }
        try {
            ReferenceMetadataType type = ReferenceMetadataType.valueOf(metadataType.toUpperCase());
            return getProvider(type);
        } catch (IllegalArgumentException e) {
            return providerMap.get(ReferenceMetadataType.DEFAULT);
        }
    }
}
