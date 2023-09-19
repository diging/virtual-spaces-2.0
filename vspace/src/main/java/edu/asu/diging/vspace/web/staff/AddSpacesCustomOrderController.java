package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class AddSpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @RequestMapping(value = "/staff/space/order/add", method = RequestMethod.GET)
    public String addSpacesCustomOrders(Model model) {
        model.addAttribute("spaces", spaceManager.getAllSpaces());
        return "staff/spaces/customorder/add";
    }
    
    @RequestMapping(value = "/staff/space/order/customorder/add", method = RequestMethod.POST)
    public String createCustomOrder(Model model, @RequestParam("spaceOrder") List<String> spaceOrders,
            @RequestParam("name") String name,
            @RequestParam("description") String description, RedirectAttributes attributes) {
        if(name == null || name.isEmpty()) {
            attributes.addFlashAttribute("alertType", "danger");
            attributes.addFlashAttribute("message", "Custom order could not be saved. Please enter the name of the order.");
            attributes.addFlashAttribute("showAlert", "true");
            return "redirect:/staff/space/order/add";
        }
        spacesCustomOrderManager.create(spaceOrders, name, description);
        attributes.addFlashAttribute("alertType", "success");
        attributes.addFlashAttribute("message", "Custom Order has been successfully added.");
        attributes.addFlashAttribute("showAlert", "true");
        return "redirect:/staff/space/order";
    }

}
