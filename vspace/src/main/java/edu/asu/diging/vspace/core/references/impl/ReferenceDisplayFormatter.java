package edu.asu.diging.vspace.core.references.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.references.IReferenceDisplayFormatter;

@Service
public class ReferenceDisplayFormatter implements IReferenceDisplayFormatter{
    
    private StringBuilder referenceDisplayText;
    
    public ReferenceDisplayFormatter() {
        referenceDisplayText = new StringBuilder();
    }

    public String getReferenceDisplayText() {
        return referenceDisplayText.toString();
    }

    @Override
    public ReferenceDisplayFormatter addMetadata(String prefix, String metadata, String suffix ) {
        if(metadata!=null && !metadata.equals("")) {
            
            this.referenceDisplayText.append(prefix!=null ? prefix : "");
            this.referenceDisplayText.append(metadata);
            this.referenceDisplayText.append(suffix!=null ? suffix : "");
        }
        return this;
    }
    
}
