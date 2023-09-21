package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ExhibitionSpaceOrderMode;
import edu.asu.diging.vspace.core.services.IExhibitionManager;


@Controller
public class UpdateStaffSpaceOrderModeController {
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @RequestMapping(value = "/staff/space/order/mode", method = RequestMethod.POST)
    public String updateSpaceOrderMode(String mode, @RequestParam ExhibitionSpaceOrderMode spaceOrderMode) {
        exhibitionManager.updateSpaceOrderMode(spaceOrderMode);
        return "redirect:/staff/space/list";
    }
}
