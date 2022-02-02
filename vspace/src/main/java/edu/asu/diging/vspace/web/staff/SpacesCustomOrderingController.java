package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpacesCustomOrderForm;

/**
 * SpacesCustomOrderingController is the controller
 * to allow custom ordering of spaces
 * @author Glen D'souza
 *
 */

@Controller
public class SpacesCustomOrderingController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    private static Logger logger = LoggerFactory.getLogger(SpacesCustomOrderingController.class);

    @RequestMapping(value = "/staff/spaceordering/add", method = RequestMethod.GET)
    public String addSpacesCustomOrders(Model model) {
        logger.info("inside add spaces custom order");
        List<ISpace> spaces = spaceManager.getAllSpaces();
        model.addAttribute("spaces", spaces);
        return "staff/spaces/customorder/add";
    }
    
    @RequestMapping(value = "/staff/spaceordering/customorder", method = RequestMethod.POST)
    public String createCustomOrder(@ModelAttribute SpacesCustomOrderForm spacesCustomOrderForm) {
        logger.info("creating custom order");
        SpacesCustomOrder spacesCustomOrder = spacesCustomOrderManager.createNewCustomOrder(spacesCustomOrderForm);
        return "redirect:/staff/spaceordering/" + spacesCustomOrder.getId();
    }
    
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
        model.addAttribute("customSpaceOrders", spacesCustomOrder);
        return "/staff/spaces/customorder/customordering";
    }
    
//    /**
//     * This method is the controller to update the custom order of spaces
//     */
//    @RequestMapping(value = "/staff/spaceordering/updateOrder/{customOrderName}", method = RequestMethod.POST)
//    public ResponseEntity<List<ISpace>> adjustCustomOrder(@RequestBody List<ISpace> spaces, @PathVariable("customOrderName") String customOrderName) {
//        spacesCustomOrderManager.updateCustomOrder(spaces, customOrderName);
//        return new ResponseEntity<List<ISpace>>(spaces, HttpStatus.OK);
//    }
    
}
