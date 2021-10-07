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

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class SpacesCustomOrderingController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @RequestMapping("/staff/spaceordering")
    public String displayCurrentOrder(Model model) {
        List<ISpace> spaces = (List<ISpace>) spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        spaces.addAll(spaceManager.getSpacesWithStatus(null));
        model.addAttribute("spaces", spaces);
        
        return "/staff/spaces/customordering";
    }
    @RequestMapping("/staff/spaceordering/changeorder")
    public String changeDisplayOrder(Model model) {
        List<ISpace> spaces = (List<ISpace>) spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        spaces.addAll(spaceManager.getSpacesWithStatus(null));
        model.addAttribute("spaces", spaces);
        
        return "/staff/spaces/customordering";
    }
    
    @RequestMapping(value = "/staff/spaceordering/update", method = RequestMethod.POST)
    public ResponseEntity<List<SpacesCustomOrder>> adjustContentOrder(@RequestBody List<SpacesCustomOrder> spacesCustomOrderList) {
        //try {
            spacesCustomOrderManager.updateCustomOrder(spacesCustomOrderList);
//        } catch (SpacesCustomOrderDoesNotExistException e) {
//            logger.warn("Spaces Custom Order Id does not exist, bad request.", e);
//            return new ResponseEntity<List<SpacesCustomOrder>>(HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<List<SpacesCustomOrder>>(spacesCustomOrderList,HttpStatus.OK);
    }

}
