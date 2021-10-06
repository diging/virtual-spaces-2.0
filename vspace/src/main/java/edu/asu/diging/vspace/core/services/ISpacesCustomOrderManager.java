package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.SpacesCustomOrder;

public interface ISpacesCustomOrderManager {
    
    Integer findMaxContentOrder(String slideId);

    void updateCustomOrder(List<SpacesCustomOrder> spacesCustomOrderCurrentList);

}
