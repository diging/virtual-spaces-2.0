package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISpacesCustomOrder extends IVSpaceElement { 
    
    public String getCustomOrderName();

    public void setCustomOrderName(String customOrderName);
    
    public List<ISpace> getCustomOrderedSpaces();
    
    public void setCustomOrderedSpaces(List<ISpace> customOrderedSpaces);

}
