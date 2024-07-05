package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;

public class DefaultLinkImageController {
    
    @Autowired
    IExhibitionManager exhibitManager;
    
    IImageService imageService;
    
    

    /**
     * To create or update the default link images.
     * 
     * @param image - the MultipartFile representing the link image to be uploaded
     * @param linkType - the type of the link for which the default image must be set
     * @param attributes - the RedirectAttributes object to add flash attributes
     * @return - a RedirectView to the exhibition configuration page
     * @throws IOException if an input or output error occurs
     */
    @RequestMapping(value = "/staff/exhibit/config/link/defaultImage", method = RequestMethod.POST)
    public String createOrUpdateLinkImage(HttpServletRequest request,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestParam(name="linkType") String linkType,
            RedirectAttributes attributes) throws IOException {
        Exhibition exhibition = (Exhibition) exhibitManager.getStartExhibition();       
        if(linkType.equals("space")) {
            IVSImage spaceDefaultImage = imageService.storeImage(image.getBytes(), image.getOriginalFilename()); 
            exhibition.setSpaceLinkDefaultImage(spaceDefaultImage);
        }
        else if(linkType.equals("module")) {
            IVSImage moduleDefaultImage = imageService.storeImage(image.getBytes(), image.getOriginalFilename());            
            exhibition.setModuleLinkDefaultImage(moduleDefaultImage);
        }
        else if(linkType.equals("external")) {
            IVSImage externalLinkDefaultImage = imageService.storeImage(image.getBytes(), image.getOriginalFilename());         
            exhibition.setExternalLinkDefaultImage(externalLinkDefaultImage);
        }
        else {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Couldn't save the default image");
            return "redirect:/staff/exhibit/config";
        }
        
        exhibition = (Exhibition) exhibitManager.storeExhibition(exhibition);
        attributes.addAttribute("exhibitId", exhibition.getId());
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");

        return "redirect:/staff/exhibit/config";
    }
}
