package edu.asu.diging.vspace.core.model;

public interface IExhibitionLanguage extends IVSpaceElement {
    
    String getCode();
    
    String getLabel();
    
    boolean isDefault();
    
    void setDefault(boolean isDefault);
    
    void setLabel(String label);
          
}
