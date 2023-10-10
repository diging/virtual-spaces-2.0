package edu.asu.diging.vspace.core.references.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.references.IReferenceDisplayMetadataRegistry;
import edu.asu.diging.vspace.core.references.ReferenceDisplayType;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.references.IReferenceDisplayProvider;
import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

@Service
public class ReferenceDisplayMetadataRegistry implements IReferenceDisplayMetadataRegistry {

    @Autowired
    private List<IReferenceDisplayProvider> referenceDisplayProviders;

    private Map<ReferenceDisplayType, IReferenceDisplayProvider> map = new HashMap<>();

    @PostConstruct
    public void init() {
        for (IReferenceDisplayProvider refMetaData : referenceDisplayProviders) {
            map.put(refMetaData.getReferenceDisplayType(), refMetaData.getReferenceDisplayText(null));
            
            
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry#getProvider
     * (edu.asu.diging.vspace.core.references .ReferenceMetadataType)
     */
    public IReferenceDisplayProvider getProvider(ReferenceDisplayType refMetaDataType) {
        return map.get(refMetaDataType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.IReferenceMetadataRegistry#getProvider(
     * String metadataType)
     */
    public IReferenceDisplayProvider getProvider(String displayType) {
        ReferenceDisplayType refMetaDataType = ReferenceDisplayType.valueOf(displayType.toUpperCase());
        return getProvider(refMetaDataType);
    }

}
