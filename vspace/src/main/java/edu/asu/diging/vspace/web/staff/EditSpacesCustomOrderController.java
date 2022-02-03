package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class EditSpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    private static Logger logger = LoggerFactory.getLogger(SpacesCustomOrderingController.class);
    
    @RequestMapping(value = "/staff/spaceordering/{customOrderId}/edit/description", method = RequestMethod.POST)
    public ResponseEntity<String> saveDescription(@RequestParam("description") String description, @PathVariable("customOrderId") String spacesCustomOrderId) {
        SpacesCustomOrder spacesCustomOrder = spacesCustomOrderManager.getSpaceCustomOrderById(spacesCustomOrderId);
        spacesCustomOrder.setDescription(description);
        spacesCustomOrderManager.updateSpacesCustomOrder(spacesCustomOrder);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/spaceordering/{customOrderId}/edit/title", method = RequestMethod.POST)
    public ResponseEntity<String> saveTitle(@RequestParam("title") String name, @PathVariable("customOrderId") String spacesCustomOrderId) {
        SpacesCustomOrder spacesCustomOrder = spacesCustomOrderManager.getSpaceCustomOrderById(spacesCustomOrderId);
        spacesCustomOrder.setCustomOrderName(name);
        spacesCustomOrderManager.updateSpacesCustomOrder(spacesCustomOrder);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/staff/spaceordering/{customOrderId}/edit/spaceorders", method = RequestMethod.POST)
    public ResponseEntity<String> saveOrder(@RequestParam("spaceOrder") List<String> spaceOrders) {
        logger.info("space order is {}", spaceOrders.get(0));
        return new ResponseEntity<String>(HttpStatus.OK);
    }


}
