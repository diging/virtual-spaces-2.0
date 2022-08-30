package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;

public interface ISpacesCustomOrderManager {

    List<SpacesCustomOrder> findAll();

    void addSpaceToCustomOrders(ISpace space);

    void saveCustomOrders(List<SpacesCustomOrder> spacesCustomOrder);

    SpacesCustomOrder getSpaceCustomOrderById(String customSpaceOrderId);

    void updateSpacesCustomOrderNameDescription(String spacesCustomOrderId, String value, String field);

    void editSpacesCustomOrder(String spacesCustomOrderId, List<String> spaceOrders);

    SpacesCustomOrder createNewCustomOrder(List<String> spaceOrders, String name, String description);

    void setExhibitionSpacesCustomOrder(String customOrderId);

    void deleteSpacesCustomOrderById(String id);

}
