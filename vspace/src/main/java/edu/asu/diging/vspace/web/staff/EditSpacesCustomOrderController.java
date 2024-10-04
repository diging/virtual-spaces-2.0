package edu.asu.diging.vspace.web.staff;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.web.staff.forms.SpacesCustomOrderForm;

@Controller
public class EditSpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;

    @RequestMapping(value = "/staff/space/order/{customOrderId}", method = RequestMethod.POST)
    public String saveOrder(
            @PathVariable("customOrderId") String spacesCustomOrderId, 
            @RequestParam("spaceOrder") List<String> spacesOrder, @Valid SpacesCustomOrderForm spacesCustomOrderForm,
            BindingResult result, Model model, RedirectAttributes attributes) {
        if(result.hasErrors()) {
            return "redirect:/staff/space/order/"+spacesCustomOrderId;
        }
        spacesCustomOrderManager.updateNameAndDescription(spacesCustomOrderId, spacesCustomOrderForm.getName(), spacesCustomOrderForm.getDescription());
        spacesCustomOrderManager.updateSpaces(spacesCustomOrderId, spacesOrder);
        attributes.addFlashAttribute("alertType", "success");
        attributes.addFlashAttribute("message", "The custom order has been updated.");
        attributes.addFlashAttribute("showAlert", "true");
        return "redirect:/staff/space/order/"+spacesCustomOrderId;
    }


}
