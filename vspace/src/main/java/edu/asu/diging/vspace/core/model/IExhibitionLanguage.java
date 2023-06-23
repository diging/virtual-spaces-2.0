package edu.asu.diging.vspace.core.model;
import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;

public interface IExhibitionLanguage extends IVSpaceElement {
    
    String getCode();
    
    String getLabel();
    
    boolean isDefault();
    
    void setDefault(boolean isDefault);
    
    void setLabel(String label);
         
}
