package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.references.impl.ReferenceDisplayFormatter;

public interface IReferenceDisplayFormatter {
    
    public String getReferenceDisplayText();

    public ReferenceDisplayFormatter addMetadata(String prefix, String metadata, String suffix );
}
