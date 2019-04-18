package edu.asu.diging.vspace.web.staff;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

@Controller
public class EditImageController {

    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private IImageFactory imageFactory;

    @RequestMapping(value = "/staff/image/{imageId}/edit", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("imageId") String imageId) {
        VSImage image = imageRepo.findById(imageId).get();
        ImageForm imageForm = new ImageForm();
        imageForm.setFileName(image.getFilename());
        imageForm.setDescription(image.getDescription());
        model.addAttribute("imageForm", imageForm);
        model.addAttribute("imageId", imageId);

        return "staff/image/edit";
    }

    @RequestMapping(value = "/staff/image/{imageId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute ImageForm imageForm, @PathVariable("imageId") String imageId,
            RedirectAttributes attributes) {
        try {
            imageFactory.editImage(imageId, imageForm);
        } catch (FileNotFoundException|FileStorageException exception) {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            exception instanceof(FileNotFoundException)?attributes.addAttribute("message", "Error occured while renaming file name. Please try again.");
            attributes.addAttribute("message", "The image id provided is invalid. Please try again");
            return "redirect:/staff/images/list/1";
        }
        return "redirect:/staff/display/image/{imageId}";
    }
}
