package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpacesCustomOrder;

public interface ISpacesCustomOrderManager {

    Iterable<SpacesCustomOrder> findAll();

    void addSpaceToCustomOrders(ISpace space);

    ISpacesCustomOrder get(String customSpaceOrderId);

    void updateNameAndDescription(String spacesCustomOrderId, String title, String description);

    void updateSpaces(String spacesCustomOrderId, List<String> spaceOrders);

    ISpacesCustomOrder create(List<String> spaceOrders, String name, String description);

    void setExhibitionSpacesCustomOrder(String customOrderId);

    void delete(String id);

}
