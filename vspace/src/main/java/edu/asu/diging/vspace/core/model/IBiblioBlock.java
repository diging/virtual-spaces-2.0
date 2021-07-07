package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.Reference;

public interface IBiblioBlock extends IContentBlock {

    void setBiblioTitle(String biblioTitle);

    String getBiblioTitle();
    
    void setDescription(String desc);

    String getDescription();
    
    void setReferenceList(List<Reference> referenceList);
    
    List<Reference> getReferenceList();

    void setId(String id);

    String getId();

}