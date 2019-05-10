package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.model.IVSImage;

@Controller
public class ImageController {
    
    @Autowired
    private ImageRepository imageRepo;

    @RequestMapping("/staff/display/image/{id}")
    public String showImage(@PathVariable String id, Model model) {
        IVSImage image = imageRepo.findById(id).get();
        model.addAttribute("image", image);
        return "staff/image";
    }
}
