package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;

public interface ISpacesCustomOrderManager {

    Iterable<SpacesCustomOrder> findAll();

    void addSpaceToCustomOrders(ISpace space);

    void saveCustomOrders(Iterable<SpacesCustomOrder> spacesCustomOrder);

    ISpacesCustomOrder getSpaceCustomOrderById(String customSpaceOrderId);

    void updateSpacesCustomOrderNameDescription(String spacesCustomOrderId, String title, String description);

    void editSpacesCustomOrder(String spacesCustomOrderId, List<String> spaceOrders);

    ISpacesCustomOrder createNewCustomOrder(List<String> spaceOrders, String name, String description);

    void setExhibitionSpacesCustomOrder(String customOrderId);

    void deleteSpacesCustomOrderById(String id);

}
