package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

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

import edu.asu.diging.vspace.core.data.SpaceRepository;
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
    
    @Autowired
    private SpaceManager spaceManager;
    
    /**
     * This method fetches all spaces
     */
    @RequestMapping(value = "/staff/spaceordering", method = RequestMethod.GET)
    public String displayAllSpaces(Model model) {
        List<ISpace> spaces = spaceManager.getAllSpaces();
        model.addAttribute("spaces", spaces);
        return "staff/spaces/customordering";
    }
    
    @RequestMapping(value = "/staff/spaceordering/customorder/{customOrderName}", method = RequestMethod.POST)
    public void createCustomOrder(@PathVariable("spaceId") String customOrderName) {
        spacesCustomOrderManager.createNewCustomOrder(customOrderName);
    }
    
    /**
     * This method is the controller to update the custom order of spaces
     */
    @RequestMapping(value = "/staff/spaceordering/updateOrder", method = RequestMethod.POST)
    public ResponseEntity<List<SpacesCustomOrder>> adjustCustomOrder(@RequestBody List<SpacesCustomOrder> spacesCustomOrderList) {
        spacesCustomOrderManager.updateCustomOrder(spacesCustomOrderList);
        return new ResponseEntity<List<SpacesCustomOrder>>(spacesCustomOrderList,HttpStatus.OK);
    }
    
}
