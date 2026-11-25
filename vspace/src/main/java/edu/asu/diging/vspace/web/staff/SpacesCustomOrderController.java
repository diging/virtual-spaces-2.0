package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

/**
 * SpacesCustomOrderController is the controller
 * to allow custom ordering of spaces
 * @author Glen D'souza
 *
 */

@Controller
public class SpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @RequestMapping(value = "/staff/space/order/{orderId}", method = RequestMethod.GET)
    public String displaySpacesCustomOrder(Model model, @PathVariable("orderId") String customSpaceOrderId) {
        ISpacesCustomOrder spacesCustomOrder = spacesCustomOrderManager.get(customSpaceOrderId);
        model.addAttribute("customSpaceOrder", spacesCustomOrder);
        model.addAttribute("selectedSpaces", spacesCustomOrder.getCustomOrderedSpaces());
        return "/staff/spaces/customorder/order";
    }
    
}
