package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class DefaultLinkImageController {
    
    private final Map<String, BiConsumer<IExhibition, IVSImage>> imageSetterMap = Map.of(
            "space", IExhibition::setSpaceLinkDefaultImage,
            "module", IExhibition::setModuleLinkDefaultImage,
            "external", IExhibition::setExternalLinkDefaultImage
    );
    
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
        
        BiConsumer<IExhibition, IVSImage> setter = imageSetterMap.get(linkType);
        if(setter == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not save default image");
            attributes.addAttribute("showAlert", "true");
        } else {
            setter.accept(exhibition, defaultImage);
            exhibition = (Exhibition) exhibitionManager.storeExhibition(exhibition);
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "success");
            attributes.addAttribute("message", "Successfully saved!");
            attributes.addAttribute("showAlert", "true");
        }
        
        return "redirect:/staff/exhibit/config";
    }
    
    /**
     * To delete the default link images.
     * 
     * @param linkType - the type of the link for which the default image must be deleted
     * @param attributes - the RedirectAttributes object to add flash attributes
     * @return - a RedirectView to the exhibition configuration page
     * @throws IOException if an input or output error occurs
     */
    @RequestMapping(value = "/staff/exhibit/config/link/defaultImage/{linkType}", method = RequestMethod.DELETE)
    public String deleteLinkImage(@PathVariable("linkType") String linkType, RedirectAttributes attributes) throws IOException {
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();        
        
        Map<String, Supplier<IVSImage>> imageGetterMap = Map.of(
                "space", exhibition::getSpaceLinkDefaultImage,
                "module", exhibition::getModuleLinkDefaultImage,
                "external", exhibition::getExternalLinkDefaultImage
        );
        
        IVSImage image = imageGetterMap.get(linkType).get();
        if (image == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not retrieve the default image prior to deleting");
            attributes.addAttribute("showAlert", "true");
        } else {
            imageService.removeImage(image.getId());
        }

        Map<String, Runnable> imageDeleterMap = Map.of(
            "space", exhibition::deleteSpaceLinkDefaultImage,
            "module", exhibition::deleteModuleLinkDefaultImage,
            "external", exhibition::deleteExternalLinkDefaultImage
        );
        
        Runnable deleteDefautImageMethod = imageDeleterMap.get(linkType);
        if (deleteDefautImageMethod == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not delete the default image");
            attributes.addAttribute("showAlert", "true");
        } else {
            deleteDefautImageMethod.run();
            exhibition = (Exhibition) exhibitionManager.storeExhibition(exhibition);
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "success");
            attributes.addAttribute("message", "Successfully deleted the default image!");
            attributes.addAttribute("showAlert", "true");
        }
        
        return "redirect:/staff/exhibit/config";
    }
    
    /**
     * To disable the default link images.
     * 
     * @param linkType - the type of the link for which the default image must be disabled
     * @param attributes - the RedirectAttributes object to add flash attributes
     * @return - a RedirectView to the exhibition configuration page
     * @throws IOException if an input or output error occurs
     */
    @RequestMapping(value = "/staff/exhibit/config/link/defaultImage/{linkType}", method = RequestMethod.PUT)
    public ResponseEntity<String> disableLinkImage(@PathVariable("linkType") String linkType, RedirectAttributes attributes) throws IOException {
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        JsonObject jsonObj = new JsonObject(); 
        
        Map<String, Supplier<IVSImage>> imageGetterMap = Map.of(
                "space", exhibition::getSpaceLinkDefaultImage,
                "module", exhibition::getModuleLinkDefaultImage,
                "external", exhibition::getExternalLinkDefaultImage
        );
        
        IVSImage image = imageGetterMap.get(linkType).get();
        if (image == null) {
            String errorMessage = "Could not retrieve the default image prior to disabling";
            return ResponseEntity.badRequest().body(errorMessage);
        }
        
        Map<String, Runnable> imageDisablerMap = Map.of(
            "space", exhibition::disableSpaceLinkDefaultImage,
            "module", exhibition::disableModuleLinkDefaultImage,
            "external", exhibition::disableExternalLinkDefaultImage
        );
        
        Runnable disableDefautImageMethod = imageDisablerMap.get(linkType);
        if (disableDefautImageMethod == null) {
            String errorMessage = "Could not disable the default image";
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            disableDefautImageMethod.run();
            exhibitionManager.storeExhibition(exhibition);
            jsonObj.addProperty("defaultImageDisableFlag", image.getDisableFlag());
        }

        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);

    }
}
