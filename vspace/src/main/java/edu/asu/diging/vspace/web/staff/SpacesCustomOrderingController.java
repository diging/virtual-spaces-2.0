package edu.asu.diging.vspace.web.staff;

import java.util.List;

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
    
    /**
     * This method fetches all spaces
     */
    @RequestMapping(value = "/staff/spaceordering", method = RequestMethod.GET)
    public String displayAllSpaceCustomOrders(Model model) {
        List<SpacesCustomOrder> spacesCustomOrder = spacesCustomOrderManager.findAll();
        model.addAttribute("spacesCustomOrder", spacesCustomOrder);
        return "staff/spaces/customordering";
    }
    
    @RequestMapping(value = "/staff/spaceordering/customorder/{customOrderName}", method = RequestMethod.POST)
    public void createCustomOrder(@PathVariable("customOrderName") String customOrderName) {
        spacesCustomOrderManager.createNewCustomOrder(customOrderName);
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
