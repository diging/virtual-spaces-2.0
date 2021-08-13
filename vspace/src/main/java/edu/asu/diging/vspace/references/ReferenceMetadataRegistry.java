package edu.asu.diging.vspace.references;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceMetadataRegistry {
    
    @Autowired
    private List<ReferenceMetadataProvider> referenceMetadataProviders;
    
    private Map<ReferenceMetadataType, ReferenceMetadataProvider> map = new HashMap<>();
    
    @PostConstruct
    public void init() {
        for(ReferenceMetadataProvider refMetaData : referenceMetadataProviders) {
            map.put(refMetaData.getReferenceMetadataType(), refMetaData);
        }
    }
    
    public ReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType) {
        return map.get(refMetaDataType);
    }
    
    public ReferenceMetadataProvider getProvider(String metadataType) {
        ReferenceMetadataType refMetaDataType = ReferenceMetadataType.valueOf(metadataType.toUpperCase());
        return getProvider(refMetaDataType);
    }
    
}
