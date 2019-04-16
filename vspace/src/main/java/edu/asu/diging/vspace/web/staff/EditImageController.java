package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

@Controller
public class EditImageController {

    @Autowired
    private ImageRepository imageRepo;

    @RequestMapping(value="/staff/space/{spaceId}/edit", method=RequestMethod.GET)
    public String show(Model model, @PathVariable("imageId") String imageId) {
        IVSImage image = imageRepo.findById(imageId).get();
        ImageForm imageForm = new ImageForm();
        imageForm.setName(image.getFilename());
        imageForm.setDescription(image.getDescription());
        model.addAttribute("imageForm", imageForm);
        model.addAttribute("imageId", imageId);
        
        return "staff/image/edit";
    }
    
    /*@RequestMapping(value="/staff/space/{spaceId}/edit", method=RequestMethod.POST)
    public String save(@ModelAttribute SpaceForm spaceForm, @PathVariable("spaceId") String spaceId) {
        ISpace space = spaceManager.getSpace(spaceId);
        space.setName(spaceForm.getName());
        space.setDescription(spaceForm.getDescription());
        
        spaceManager.storeSpace(space, null, null);
        return "redirect:/staff/space/{spaceId}";
    }*/
}
