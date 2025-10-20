package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IBiblioBlock extends IContentBlock {

    void setBiblioTitle(String biblioTitle);

    String getBiblioTitle();
    
    void setDescription(String desc);

    String getDescription();
    
    void setReferences(List<Reference> references);
    
    List<Reference> getReferences();

    void setId(String id);

    String getId();
    
    /**
     * Renders the bibliography block with APA-formatted references
     * 
     * @return HTML formatted string with APA-style references
     */
    String renderAPAReferences();
    
    /**
     * Renders the bibliography block with raw reference data (for editing)
     * 
     * @return HTML formatted string with raw reference data
     */
    String renderRawReferences();

}