package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class EditSpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;

    @RequestMapping(value = "/staff/space/order/{customOrderId}", method = RequestMethod.POST)
    public String saveOrder(
            @PathVariable("customOrderId") String spacesCustomOrderId, 
            @RequestParam("spaceOrder") List<String> spaceOrders, 
            @RequestParam("title") String title, 
            @RequestParam("description") String description,
            RedirectAttributes attributes) {
        if(title == null || title.isEmpty()) {
            attributes.addFlashAttribute("alertType", "danger");
            attributes.addFlashAttribute("message", "Custom order could not be updated. The title field is empty.");
            attributes.addFlashAttribute("showAlert", "true");
            return "redirect:/staff/space/order/"+spacesCustomOrderId;
        }
        spacesCustomOrderManager.updateNameAndDescription(spacesCustomOrderId, title, description);
        spacesCustomOrderManager.updateSpaces(spacesCustomOrderId, spaceOrders);
        attributes.addFlashAttribute("alertType", "success");
        attributes.addFlashAttribute("message", "The custom order has been updated.");
        attributes.addFlashAttribute("showAlert", "true");
        return "redirect:/staff/space/order/"+spacesCustomOrderId;
    }


}
