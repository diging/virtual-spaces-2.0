package edu.asu.diging.vspace.core.references.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.references.IReferenceMetadataFormatRegistry;
import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

@Service
public class ReferenceMetadataFormatRegistry implements IReferenceMetadataFormatRegistry {

    @Autowired
    private List<IReferenceMetadataProvider> referenceMetadataProviders;

    private Map<ReferenceMetadataType, IReferenceMetadataProvider> map = new HashMap<>();

    @PostConstruct
    public void init() {
        for (IReferenceMetadataProvider refMetaData : referenceMetadataProviders) {
            map.put(refMetaData.getReferenceMetadataType(), refMetaData);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry#getProvider
     * (edu.asu.diging.vspace.core.references .ReferenceMetadataType)
     */
    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType) {
        return map.get(refMetaDataType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry#getProvider(
     * String metadataType)
     */
    public IReferenceMetadataProvider getProvider(String metadataType) {
        ReferenceMetadataType refMetaDataType = ReferenceMetadataType.valueOf(metadataType.toUpperCase());
        return getProvider(refMetaDataType);
    }

}
