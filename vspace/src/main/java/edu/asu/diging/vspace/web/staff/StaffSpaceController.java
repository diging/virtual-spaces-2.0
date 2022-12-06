package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ExhibitionSpaceOrderMode;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

/**
 * StaffSpaceController is the controller
 * to set mode of order of spaces on exhibition side, change custom order selection
 * @author Tushar Anand
 *
 */

@Controller
public class StaffSpaceController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @RequestMapping(value = "/staff/space/order", method = RequestMethod.GET)
    public String displayCustomOrders(Model model) {
        model.addAttribute("customSpaceOrders", spacesCustomOrderManager.findAll());
        model.addAttribute("currentSelectedCustomOrder", exhibitionManager.getStartExhibition().getSpacesCustomOrder());
        return "/staff/spaces/customorder/customordering";
    }
    
    @RequestMapping(value = "/staff/space/order/setExhibitionCustomOrder", method = RequestMethod.POST)
    public String setExhibitionSpacesCustomOrder(Model model,
            @RequestParam("selectedCustomOrderId") String orderId) {
        spacesCustomOrderManager.setExhibitionSpacesCustomOrder(orderId);
        return "redirect:/staff/space/order";
    }
    
    @RequestMapping(value = "/staff/space/order/mode", method = RequestMethod.POST)
    public String updateSpaceOrderMode(String mode, @RequestParam ExhibitionSpaceOrderMode spaceOrderMode) {
        exhibitionManager.updateSpaceOrderMode(spaceOrderMode);
        return "redirect:/staff/space/list";
    }
    
}
