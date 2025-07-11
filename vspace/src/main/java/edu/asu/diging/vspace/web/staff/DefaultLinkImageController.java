package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class DefaultLinkImageController {
    
    private final Map<String, BiConsumer<Exhibition, IVSImage>> imageSetterMap = Map.of(
            "space", Exhibition::setSpaceLinkDefaultImage,
            "module", Exhibition::setModuleLinkDefaultImage,
            "external", Exhibition::setExternalLinkDefaultImage);
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private IImageService imageService;   

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
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        IVSImage defaultImage = imageService.storeImage(image.getBytes(), image.getOriginalFilename());
        
        BiConsumer<Exhibition, IVSImage> setter = imageSetterMap.get(linkType);
        if(setter == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not save default image");
            attributes.addAttribute("showAlert", "true");
            return "redirect:/staff/exhibit/config";
        }
        setter.accept(exhibition, defaultImage);
        exhibition = (Exhibition) exhibitionManager.storeExhibition(exhibition);
        attributes.addAttribute("exhibitId", exhibition.getId());
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");

        return "redirect:/staff/exhibit/config";
    }
}
