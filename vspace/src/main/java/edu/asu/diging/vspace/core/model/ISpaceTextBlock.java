package edu.asu.diging.vspace.core.model;

public interface ISpaceTextBlock extends IVSpaceElement{
    
    ISpace getSpace();

    void setSpace(ISpace space);
    
    String getText();

    void setText(String text);
}
