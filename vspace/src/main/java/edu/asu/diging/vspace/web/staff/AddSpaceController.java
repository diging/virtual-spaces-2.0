package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class AddSpaceController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceFactory spaceFactory;
    
    @Autowired
    private IImageService imageService;
    
    @Autowired
    private ImageRepository imageRepo;

    @RequestMapping(value = "/staff/space/add", method = RequestMethod.GET)
    public String showAddSpace(Model model) {
        model.addAttribute("space", new SpaceForm());
        model.addAttribute("images", imageService.getImages(1));

        return "staff/space/add";
    }

    @RequestMapping(value = "/staff/space/add", method = RequestMethod.POST)
    public String addSpace(Model model, @ModelAttribute SpaceForm spaceForm, @RequestParam("file") MultipartFile file,
            Principal principal, @RequestParam(value = "", required=false) String imageID) throws IOException {
        ISpace space = spaceFactory.createSpace(spaceForm);
        byte[] bgImage = null;
        String filename = null;
        if (file != null) {
            bgImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        
        if(!"".equalsIgnoreCase(imageID)) {
            Optional<VSImage> imgContainer = imageRepo.findById(imageID);
            VSImage img = imgContainer.get();
            spaceManager.storeSpace(space, img);
        }else {
            spaceManager.storeSpace(space, bgImage, filename);
        }
        

        return "redirect:/staff/space/list";
    }

}
