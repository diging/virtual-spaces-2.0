package edu.asu.diging.vspace.core.references.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry;
import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

@Service
public class ReferenceMetadataRegistry implements IReferenceMetadataRegistry {
    
    @Autowired
    private List<IReferenceMetadataProvider> referenceMetadataProviders;
    
    private Map<ReferenceMetadataType, IReferenceMetadataProvider> map = new HashMap<>();
    
    @PostConstruct
    public void init() {
        for(IReferenceMetadataProvider refMetaData : referenceMetadataProviders) {
            map.put(refMetaData.getReferenceMetadataType(), refMetaData);
        }
    }
    
    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType) {
        return map.get(refMetaDataType);
    }
    
    public IReferenceMetadataProvider getProvider(String metadataType) {
        ReferenceMetadataType refMetaDataType = ReferenceMetadataType.valueOf(metadataType.toUpperCase());
        return getProvider(refMetaDataType);
    }
    
}
