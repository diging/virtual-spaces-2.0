package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
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
    private ISpaceManager spaceManager;
    
    private static Logger logger = LoggerFactory.getLogger(SpacesCustomOrderController.class);
    
    @RequestMapping(value = "/staff/spaceordering/{orderId}", method = RequestMethod.GET)
    public String displaySpacesCustomOrder(Model model, @PathVariable("orderId") String customSpaceOrderId) {
        SpacesCustomOrder spacesCustomOrder = spacesCustomOrderManager.getSpaceCustomOrderById(customSpaceOrderId);
        List<ISpace> spaces = spaceManager.getAllSpaces();
        model.addAttribute("customSpaceOrder", spacesCustomOrder);
        model.addAttribute("selectedSpaces", spacesCustomOrder.getCustomOrderedSpaces());
        model.addAttribute("allSpaces", spaces);
        return "/staff/spaces/customorder/order";
    }
    
    @RequestMapping(value = "/staff/spaceordering", method = RequestMethod.GET)
    public String displayCustomOrders(Model model) {
        List<SpacesCustomOrder> spacesCustomOrder = spacesCustomOrderManager.findAll();
        SpacesCustomOrder currentExhibitionCurrentSpacesCustomOrder = spacesCustomOrderManager.
                getExhibitionCurrentSpacesCustomOrder();
        model.addAttribute("customSpaceOrders", spacesCustomOrder);
        model.addAttribute("currentSelectedCustomOrder", currentExhibitionCurrentSpacesCustomOrder);
        return "/staff/spaces/customorder/customordering";
    }
    
    @RequestMapping(value = "/staff/spaceordering/setDefault", method = RequestMethod.POST)
    public String setExhibitionSpacesCustomOrder(Model model,
            @RequestParam("selectedCustomOrderId") String sequenceId) {
        logger.info("space id is {}", sequenceId);
        spacesCustomOrderManager.setExhibitionSpacesCustomOrder(sequenceId);
        return "redirect:/staff/spaceordering";
    }
    

    
}
