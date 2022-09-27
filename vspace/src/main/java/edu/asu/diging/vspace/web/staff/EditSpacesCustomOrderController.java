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

import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class EditSpacesCustomOrderController {
    
    private static Logger logger = LoggerFactory.getLogger(SpacesCustomOrderController.class);
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @RequestMapping(value = "/staff/space/order/{customOrderId}/edit/info", method = RequestMethod.POST)
    public ResponseEntity<String> saveTitleDescription(@RequestParam("title") String title, @RequestParam("description") String description, @PathVariable("customOrderId") String spacesCustomOrderId) {
        spacesCustomOrderManager.updateSpacesCustomOrderNameDescription(spacesCustomOrderId, title, description);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/space/order/{customOrderId}/edit/spaceorders", method = RequestMethod.POST)
    public ResponseEntity<String> saveOrder(@PathVariable("customOrderId") String spacesCustomOrderId, @RequestParam("spaceOrder") List<String> spaceOrders) {
        logger.info("space order is {}", spaceOrders.get(0));
        spacesCustomOrderManager.editSpacesCustomOrder(spacesCustomOrderId, spaceOrders);
        return new ResponseEntity<String>(HttpStatus.OK);
    }


}
