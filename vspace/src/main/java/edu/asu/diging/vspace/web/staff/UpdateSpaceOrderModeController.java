package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ExhibitionSpaceOrderMode;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;

@Controller
public class UpdateSpaceOrderModeController {
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;
    
    @RequestMapping(value = "/staff/space/order/mode", method = RequestMethod.POST)
    public String updateSpaceOrderMode(String mode, @RequestParam String spaceOrderMode) {
        if(ExhibitionSpaceOrderMode.valueOf(spaceOrderMode)==ExhibitionSpaceOrderMode.CUSTOM) {
            spacesCustomOrderManager.setExhibitionSpacesCustomOrder(spaceOrderMode);
            exhibitionManager.updateSpaceOrderMode(ExhibitionSpaceOrderMode.CUSTOM);
        }
        else {
            exhibitionManager.updateSpaceOrderMode(ExhibitionSpaceOrderMode.valueOf(spaceOrderMode));
        }
        
        return "redirect:/staff/exhibit/config";
    }
}
