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

    private static final String LINK_TYPE_SPACE = "space";
    private static final String LINK_TYPE_MODULE = "module";
    private static final String LINK_TYPE_EXTERNAL = "external";

    private final Map<String, BiConsumer<IExhibition, IVSImage>> imageSetterMap = Map.of(
            LINK_TYPE_SPACE, IExhibition::setSpaceLinkDefaultImage,
            LINK_TYPE_MODULE, IExhibition::setModuleLinkDefaultImage,
            LINK_TYPE_EXTERNAL, IExhibition::setExternalLinkDefaultImage
    );
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private IImageService imageService;

    /**
     * Helper method to create image getter map for a specific exhibition instance.
     *
     * @param exhibition - the exhibition instance
     * @return - a map of link types to their corresponding image getter methods
     */
    private Map<String, Supplier<IVSImage>> getImageGetterMap(IExhibition exhibition) {
        return Map.of(
                LINK_TYPE_SPACE, exhibition::getSpaceLinkDefaultImage,
                LINK_TYPE_MODULE, exhibition::getModuleLinkDefaultImage,
                LINK_TYPE_EXTERNAL, exhibition::getExternalLinkDefaultImage
        );
    }

    /**
     * Helper method to create image deleter map for a specific exhibition instance.
     *
     * @param exhibition - the exhibition instance
     * @return - a map of link types to their corresponding image deleter methods
     */
    private Map<String, Runnable> getImageDeleterMap(IExhibition exhibition) {
        return Map.of(
                LINK_TYPE_SPACE, exhibition::deleteSpaceLinkDefaultImage,
                LINK_TYPE_MODULE, exhibition::deleteModuleLinkDefaultImage,
                LINK_TYPE_EXTERNAL, exhibition::deleteExternalLinkDefaultImage
        );
    }

    /**
     * Helper method to create image disabler map for a specific exhibition instance.
     *
     * @param exhibition - the exhibition instance
     * @return - a map of link types to their corresponding image disabler methods
     */
    private Map<String, Runnable> getImageDisablerMap(IExhibition exhibition) {
        return Map.of(
                LINK_TYPE_SPACE, exhibition::disableSpaceLinkDefaultImage,
                LINK_TYPE_MODULE, exhibition::disableModuleLinkDefaultImage,
                LINK_TYPE_EXTERNAL, exhibition::disableExternalLinkDefaultImage
        );
    }

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
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        IVSImage defaultImage = imageService.storeImage(image.getBytes(), image.getOriginalFilename());
        
        BiConsumer<IExhibition, IVSImage> setter = imageSetterMap.get(linkType);
        if(setter == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not save default image");
            attributes.addAttribute("showAlert", "true");
        } else {
            setter.accept(exhibition, defaultImage);
            exhibition = exhibitionManager.storeExhibition(exhibition);
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
        IExhibition exhibition = exhibitionManager.getStartExhibition();

        Map<String, Supplier<IVSImage>> imageGetterMap = getImageGetterMap(exhibition);

        IVSImage image = imageGetterMap.get(linkType).get();
        if (image == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not retrieve the default image prior to deleting");
            attributes.addAttribute("showAlert", "true");
        } else {
            imageService.removeImage(image.getId());
        }

        Map<String, Runnable> imageDeleterMap = getImageDeleterMap(exhibition);
        Runnable deleteDefautImageMethod = imageDeleterMap.get(linkType);
        if (deleteDefautImageMethod == null) {
            attributes.addAttribute("exhibitId", exhibition.getId());
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", "Could not delete the default image");
            attributes.addAttribute("showAlert", "true");
        } else {
            deleteDefautImageMethod.run();
            exhibition = exhibitionManager.storeExhibition(exhibition);
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
        // Validate linkType first
        if (!imageSetterMap.containsKey(linkType)) {
            String errorMessage = "Invalid link type: " + linkType;
            return ResponseEntity.badRequest().body(errorMessage);
        }
        
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        JsonObject jsonObj = new JsonObject();

        Map<String, Supplier<IVSImage>> imageGetterMap = getImageGetterMap(exhibition);

        IVSImage image = imageGetterMap.get(linkType).get();
        if (image == null) {
            String errorMessage = "Could not retrieve the default image prior to disabling";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        Map<String, Runnable> imageDisablerMap = getImageDisablerMap(exhibition);

        Runnable disableDefautImageMethod = imageDisablerMap.get(linkType);
        disableDefautImageMethod.run();
        exhibitionManager.storeExhibition(exhibition);
        jsonObj.addProperty("defaultImageDisableFlag", image.getDisableFlag());

        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);

    }
}
