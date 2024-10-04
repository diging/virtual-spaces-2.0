package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

/**
 * ExhibitionSpacesCustomOrdersController is used for displaying all the custom orders 
 * and setting the custom order of spaces in an exhibition. This controller allows staff to 
 * set a defined custom order and view the list of all custom orders 
 * 
 * @author Glen D'souza
 *
 */
@Controller
public class ExhibitionCustomOrdersController {
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @RequestMapping(value = "/staff/space/order", method = RequestMethod.GET)
    public String displayAllSpacesCustomOrders(Model model) {
        model.addAttribute("customSpaceOrders", spacesCustomOrderManager.findAll());
        model.addAttribute("currentSelectedCustomOrder", exhibitionManager.getStartExhibition().getSpacesCustomOrder());
        return "/staff/spaces/customorder/customordering";
    }
    
    @RequestMapping(value = "/staff/space/order", method = RequestMethod.POST)
    public String setExhibitionSpacesCustomOrder(Model model,
            @RequestParam("selectedCustomOrderId") String orderId) {
        spacesCustomOrderManager.setExhibitionSpacesCustomOrder(orderId);
        return "redirect:/staff/space/order";
    }

}
