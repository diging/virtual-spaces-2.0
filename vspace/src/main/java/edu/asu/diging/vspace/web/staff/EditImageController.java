package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

@Controller
public class EditImageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IImageService imageService;

    @RequestMapping(value = "/staff/image/{imageId}/edit", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("imageId") String imageId, RedirectAttributes attributes) {
        try {
            IVSImage image = imageService.getImageById(imageId);
            ImageForm imageForm = new ImageForm();
            imageForm.setFileName(image.getFilename());
            imageForm.setName(image.getName());
            imageForm.setDescription(image.getDescription());
            model.addAttribute("imageForm", imageForm);
            model.addAttribute("imageId", imageId);
        } catch(ImageDoesNotExistException imageDoesNotExistException) {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Image doesnt exist with given image id.");
            return "redirect:/staff/images/list/1";  
        }
        return "staff/image/edit";
    }

    @RequestMapping(value = "/staff/image/{imageId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute ImageForm imageForm, @PathVariable("imageId") String imageId,
            RedirectAttributes attributes) {
        try {
            imageService.editImage(imageId, imageForm);
        } catch (ImageDoesNotExistException imageDoesNotExistException) {
            logger.error("Edit Image Failed" + imageDoesNotExistException);
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Edit Image Failed. Please try again");
            return "redirect:/staff/images/list/1";
        }
        return "redirect:/staff/display/image/{imageId}";
    }
}
