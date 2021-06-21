package edu.asu.diging.vspace.biblioExpose;

import edu.asu.diging.vspace.core.model.IBiblioBlock;

public class BiblioContext {
    
    private ICitationStyle citationStyle;
    
    private IBiblioBlock biblioBlock;

    public BiblioContext(ICitationStyle citationStyle, IBiblioBlock biblioBlock) {
        this.citationStyle = citationStyle;
        this.biblioBlock = biblioBlock;
    }
    
    public IBiblioMetadata getBiblioMetadataConverter() {
        //We can introduce switch case for different types of metadata/citation style to return appropriate IBiblioMetadata
        //For now its default
        IBiblioMetadata biblioMetadata = new ZoteroDefaultMetaData(citationStyle, biblioBlock);
        return biblioMetadata;
    }

    public String  executeBiblioMetadata(IBiblioBlock biblioBlock) {
        IBiblioMetadata biblioMetadata = this.getBiblioMetadataConverter();
        return biblioMetadata.createMetadata(citationStyle, biblioBlock);
    }
}
