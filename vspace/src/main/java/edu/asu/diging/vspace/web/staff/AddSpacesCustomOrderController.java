package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class AddSpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    private static Logger logger = LoggerFactory.getLogger(AddSpacesCustomOrderController.class);
    
    @RequestMapping(value = "/staff/spaceordering/add", method = RequestMethod.GET)
    public String addSpacesCustomOrders(Model model) {
        logger.info("inside add spaces custom order");
        List<ISpace> spaces = spaceManager.getAllSpaces();
        model.addAttribute("spaces", spaces);
        return "staff/spaces/customorder/add";
    }
    
    @RequestMapping(value = "/staff/spaceordering/customorder/add", method = RequestMethod.POST)
    public ResponseEntity<String> createCustomOrder(Model model, @RequestParam("spaceOrder") List<String> spaceOrders,
            @RequestParam("name") String name,
            @RequestParam("description") String description) {
        logger.info("creating custom order");
        logger.info("name {}", name);
        logger.info("description {}", description);
        SpacesCustomOrder spacesCustomOrder = null;
        try {
            spacesCustomOrder = spacesCustomOrderManager.createNewCustomOrder(spaceOrders, name, description);
        }
        catch(IllegalStateException ex) {
            model.addAttribute("showAlert", true);
            return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
        }
        model.addAttribute("showAlert", false);
        return new ResponseEntity<String>(spacesCustomOrder.getId(), HttpStatus.OK);
    }

}
