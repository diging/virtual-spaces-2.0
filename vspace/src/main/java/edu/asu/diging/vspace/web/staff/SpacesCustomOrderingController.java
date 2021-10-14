package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class SpacesCustomOrderingController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @RequestMapping("/staff/spaceordering")
    public String displayCurrentOrderLanding(Model model) {
        spacesCustomOrderManager.persistPublishedSpacesToSpacesCustomOrder();
        logger.info("before spaces are added");
        List<SpacesCustomOrder> spaces = spacesCustomOrderManager.findAll();
        model.addAttribute("spaces", spaces);
        logger.info("spaces are added");
        return "staff/spaces/customordering";
    }
    
    @RequestMapping(value = "/staff/spaceordering/updateOrder", method = RequestMethod.POST)
    public ResponseEntity<List<SpacesCustomOrder>> adjustContentOrder(@RequestBody List<SpacesCustomOrder> spacesCustomOrderList) {
        spacesCustomOrderManager.updateCustomOrder(spacesCustomOrderList);
        return new ResponseEntity<List<SpacesCustomOrder>>(spacesCustomOrderList,HttpStatus.OK);
    }
    
}
