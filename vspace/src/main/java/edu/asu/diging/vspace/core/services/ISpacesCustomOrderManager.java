package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public interface ISpacesCustomOrderManager {
    
    Integer findMaxCustomOrder(String slideId);

    void updateCustomOrder(List<SpacesCustomOrder> spacesCustomOrderCurrentList);
    
    void persistPublishedSpacesToSpacesCustomOrder();

    List<SpacesCustomOrder> findAll();

    void updateStatusChange(ISpace space, SpaceStatus status);

    void updateStatusChangeToUnpublished(ISpace space);
    
    void updateStatusChangeToPublished(ISpace space);

}
