package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
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
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @RequestMapping(value = "/staff/space/order/{orderId}", method = RequestMethod.GET)
    public String displaySpacesCustomOrder(Model model, @PathVariable("orderId") String customSpaceOrderId) {
        SpacesCustomOrder spacesCustomOrder = spacesCustomOrderManager.getSpaceCustomOrderById(customSpaceOrderId);
        model.addAttribute("customSpaceOrder", spacesCustomOrder);
        model.addAttribute("selectedSpaces", spacesCustomOrder.getCustomOrderedSpaces());
        return "/staff/spaces/customorder/order";
    }
    
    @RequestMapping(value = "/staff/space/order", method = RequestMethod.GET)
    public String displayCustomOrders(Model model) {
        List<SpacesCustomOrder> spacesCustomOrder = spacesCustomOrderManager.findAll();
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        SpacesCustomOrder currentExhibitionCurrentSpacesCustomOrder = exhibition.getSpacesCustomOrder();
        model.addAttribute("customSpaceOrders", spacesCustomOrder);
        model.addAttribute("currentSelectedCustomOrder", currentExhibitionCurrentSpacesCustomOrder);
        return "/staff/spaces/customorder/customordering";
    }
    
    @RequestMapping(value = "/staff/space/order/setExhibitionCustomOrder", method = RequestMethod.POST)
    public String setExhibitionSpacesCustomOrder(Model model,
            @RequestParam("selectedCustomOrderId") String orderId) {
        spacesCustomOrderManager.setExhibitionSpacesCustomOrder(orderId);
        return "redirect:/staff/space/order";
    }
    

    
}
