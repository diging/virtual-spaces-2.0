package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class DeleteSpacesCustomOrderController {
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @DeleteMapping(value = "/staff/space/customorder/{orderId}")
    public ResponseEntity<String> deleteSpacesCustomOrder(@PathVariable String orderId, Model model, RedirectAttributes attributes) {
        spacesCustomOrderManager.delete(orderId);
        attributes.addFlashAttribute("alertType", "success");
        attributes.addFlashAttribute("message", "Custom Order has been successfully deleted.");
        attributes.addFlashAttribute("showAlert", "true");
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
