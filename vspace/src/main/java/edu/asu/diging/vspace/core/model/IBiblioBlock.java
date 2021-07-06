package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IBiblioBlock extends IContentBlock {

    void setBiblioTitle(String biblioTitle);

    String getBiblioTitle();
    
    void setDescription(String desc);

    String getDescription();
    
    void setReferenceList(List<IReference> referenceList);
    
    List<IReference> getReferenceList();

    void setId(String id);

    String getId();

}