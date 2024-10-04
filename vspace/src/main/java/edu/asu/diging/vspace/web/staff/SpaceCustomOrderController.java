package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

/**
 * SpaceCustomOrderController is used for managing and displaying the custom order
 * of spaces in an exhibition. This controller allows staff to view as defined by custom configurations.
 * 
 * @author Glen D'souza
 *
 */
@Controller
public class SpaceCustomOrderController {
    
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
