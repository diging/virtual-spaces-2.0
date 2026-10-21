package edu.asu.diging.vspace.core.model;


public interface IExhibitionLanguage extends IVSpaceElement {
    
    String getCode();
    
    String getLabel();
    
    void setLabel(String label);
    
    boolean isDefault();
    
    void setDefault(boolean isDefault);   

}
