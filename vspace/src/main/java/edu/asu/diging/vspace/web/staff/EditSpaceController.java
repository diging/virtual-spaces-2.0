package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class EditSpaceController {

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;

    @RequestMapping(value="/staff/space/{spaceId}/edit", method=RequestMethod.GET)
    public String show(Model model, @PathVariable("spaceId") String spaceId) {

        model.addAttribute("spaceForm", spaceManager.getSpaceForm(spaceId));
        model.addAttribute("spaceId", spaceId);
        
        IExhibition startExhibtion = exhibitionManager.getStartExhibition();   
        
        model.addAttribute("languageObjectList" , startExhibtion.getLanguages());

        return "staff/spaces/edit";
    }
    
    @RequestMapping(value="/staff/space/{spaceId}/edit", method=RequestMethod.POST)
    public String save(@ModelAttribute SpaceForm spaceForm, @PathVariable("spaceId") String spaceId) {
        ISpace space = spaceManager.getSpace(spaceId);
        space.setName(spaceForm.getName());
        space.setDescription(spaceForm.getDescription());
        spaceManager.updateNameAndDescription(space, spaceForm);
        spaceManager.storeSpace(space, null, null);
        return "redirect:/staff/space/{spaceId}";
    }
}
