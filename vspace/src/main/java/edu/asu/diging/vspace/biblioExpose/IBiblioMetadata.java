package edu.asu.diging.vspace.biblioExpose;

import edu.asu.diging.vspace.core.model.IBiblioBlock;

public interface IBiblioMetadata {
    public String getBiblioMetadataStyle();
    public String createMetadata(ICitationStyle citationStyle, IBiblioBlock biblioBlock);
}
