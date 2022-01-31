package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public interface ISpacesCustomOrderManager {

    List<SpacesCustomOrder> findAll();

    void createNewCustomOrder(String customOrderName);

    void addSpaceToCustomOrders(ISpace space);

    void saveCustomOrders(List<SpacesCustomOrder> spacesCustomOrder);

}
