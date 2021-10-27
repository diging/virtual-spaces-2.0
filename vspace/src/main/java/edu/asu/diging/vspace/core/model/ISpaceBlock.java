package edu.asu.diging.vspace.core.model;

public interface ISpaceBlock {
    
    void setTitle(String text);

    String getTitle();

    void setId(String id);

    String getId();
    
    ISpace getSpace();
    
    void setSpace(ISpace space);
    
}
