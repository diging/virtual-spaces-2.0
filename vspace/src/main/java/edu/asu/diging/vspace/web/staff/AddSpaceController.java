package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.core.services.impl.SpacesCustomOrderManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class AddSpaceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceFactory spaceFactory;

    @Autowired
    private IImageService imageService;
    
    @Autowired
    private ISpacesCustomOrderManager spacesCustomOrderManager;


    @RequestMapping(value = "/staff/space/add", method = RequestMethod.GET)
    public String showAddSpace(Model model) {
        model.addAttribute("space", new SpaceForm());
        model.addAttribute("images", imageService.getImages(1));

        return "staff/spaces/add";
    }

    @RequestMapping(value = "/staff/space/add", method = RequestMethod.POST)
    public String addSpace(Model model, @ModelAttribute SpaceForm spaceForm, @RequestParam("file") MultipartFile file,
            Principal principal, @RequestParam(value = "imageId", required=false) String imageId, RedirectAttributes redirectAttrs) throws IOException {
        ISpace space = spaceFactory.createSpace(spaceForm);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        byte[] bgImage = null;
        String filename = null;
        if (file != null) {
            bgImage = file.getBytes();
            filename = file.getOriginalFilename();
        } 
        CreationReturnValue creationValue = null;
        if(imageId != null && !imageId.isEmpty()) {
            IVSImage image;
            try {
                image = imageService.getImageById(imageId);
            } catch (ImageDoesNotExistException e) {
                logger.error("Image does not exist.", e);
                redirectAttrs.addAttribute("showAlert", true);
                redirectAttrs.addAttribute("alertType", "danger");
                redirectAttrs.addAttribute("message", "Selected image does not exist.");
                return "redirect:/staff/spaces/spacelist";
            }
            creationValue = spaceManager.storeSpace(space, image);
        }else {
            creationValue = spaceManager.storeSpace(space, bgImage, filename);
        }
        
        //add new space to all custom space orders
        spacesCustomOrderManager.addSpaceToCustomOrders(space);
        
        if (creationValue != null) {
            return "redirect:/staff/space/" + creationValue.getElement().getId();
        }

        redirectAttrs.addAttribute("showAlert", true);
        redirectAttrs.addAttribute("alertType", "danger");
        redirectAttrs.addAttribute("message", "Unkown error. Space could not be created.");
        return "redirect:/staff/spaces/spacelist";
    }

}
