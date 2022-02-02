package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.web.staff.forms.SpacesCustomOrderForm;

public interface ISpacesCustomOrderManager {

    List<SpacesCustomOrder> findAll();

    SpacesCustomOrder createNewCustomOrder(SpacesCustomOrderForm spacesCustomOrderForm);

    void addSpaceToCustomOrders(ISpace space);

    void saveCustomOrders(List<SpacesCustomOrder> spacesCustomOrder);

    SpacesCustomOrder getSpaceCustomOrderById(String customSpaceOrderId);

    void updateSpacesCustomOrder(SpacesCustomOrder spacesCustomOrder);

}
