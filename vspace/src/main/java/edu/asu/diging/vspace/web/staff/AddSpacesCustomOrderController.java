package edu.asu.diging.vspace.web.staff;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.web.staff.forms.SpacesCustomOrderForm;

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
    public String createCustomOrder(Model model, @Valid @ModelAttribute SpacesCustomOrderForm spacesCustomOrderForm, 
            BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("spacesCustomOrderForm",spacesCustomOrderForm);
            model.addAttribute("spaces", spaceManager.getAllSpaces());
            return "staff/spaces/customorder/add";
        }
        spacesCustomOrderManager.create(spacesCustomOrderForm.getOrderedSpaces(), spacesCustomOrderForm.getName(), spacesCustomOrderForm.getDescription());
        attributes.addFlashAttribute("alertType", "success");
        attributes.addFlashAttribute("message", "Custom Order has been successfully added.");
        attributes.addFlashAttribute("showAlert", "true");
        return "redirect:/staff/space/order";
    }

}
