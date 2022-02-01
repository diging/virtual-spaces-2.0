package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

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
    
    /**
     * This method fetches all spaces
     */
    @RequestMapping(value = "/staff/spaceordering/add", method = RequestMethod.GET)
    public String addSpacesCustomOrders(Model model) {
        logger.info("inside add spaces custom order");
        List<ISpace> spaces = spaceManager.getAllSpaces();
        model.addAttribute("spaces", spaces);
        return "staff/spaces/customorder/add";
    }
    
    @RequestMapping(value = "/staff/spaceordering/customorder", method = RequestMethod.POST)
    public void createCustomOrder(@PathVariable("customOrderName") String customOrderName) {
        spacesCustomOrderManager.createNewCustomOrder(customOrderName);
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
