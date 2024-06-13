package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.javers.common.collections.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ExhibitionConfigurationController {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IExhibitionManager exhibitManager;

    @Autowired
    private ExhibitionFactory exhibitFactory;

    @Autowired
    private IImageService imageService;

    @Autowired
    private ExhibitionLanguageConfig exhibitionLanguageConfig;

    public static final String EXH_PREVIEW = "EXH_PREVIEW_";

    @RequestMapping("/staff/exhibit/config")
    public String showExhibitions(Model model) {
        // for now we assume there is just one exhibition

        IExhibition exhibition = exhibitManager.getStartExhibition();
        if (exhibition==null) {
            exhibition = (Exhibition) exhibitFactory.createExhibition();
        }
        if (exhibition.getLanguages() != null) {
            model.addAttribute("savedExhibitionLanguages", exhibition.getLanguages()
                    .stream().map(language -> language.getLabel()).collect(Collectors.toList()));
            model.addAttribute("defaultLanguage", exhibition.getLanguages()
                    .stream().filter(language -> language.isDefault()).findFirst().orElse(null));
        }
        model.addAttribute("exhibitionModes", Arrays.asList(ExhibitionModes.values()));
        model.addAttribute("spacesList", spaceRepo.findAll());
        model.addAttribute("languageList", exhibitionLanguageConfig.getExhibitionLanguageList());
        model.addAttribute("exhibition", exhibition);
        model.addAttribute("defaultSpaceLinkImage",exhibition.getSpaceLinkDefaultImage());
        model.addAttribute("defaultModuleLinkImage",exhibition.getModuleLinkDefaultImage());
        model.addAttribute("defaultExternalLinkImage",exhibition.getExternalLinkDefaultImage());
        return "staff/exhibit/config";
    }

    /**
     * exhibitionParam is used when default space of existing exhibition is updated.
     * 
     * @param exhibitionParam
     * @param spaceParam
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/staff/exhibit/config", method = RequestMethod.POST)
    public RedirectView createOrUpdateExhibition(HttpServletRequest request,
            @RequestParam(required = false, name = "exhibitionParam") String exhibitID,
            @RequestParam("spaceParam") String spaceID, @RequestParam("title") String title,
            @RequestParam("exhibitMode") ExhibitionModes exhibitMode,
            @RequestParam(value = "customMessage", required = false, defaultValue = "") String customMessage,
            @RequestParam("exhibitLanguage") List<String> languages,
            @RequestParam("defaultExhibitLanguage") String defaultLanguage,

            RedirectAttributes attributes) throws IOException {

        ISpace startSpace = spaceManager.getSpace(spaceID);
        
        Exhibition exhibition;
        
        if (exhibitID == null || exhibitID.isEmpty()) {
            exhibition = (Exhibition) exhibitFactory.createExhibition();
        } else {
            exhibition = (Exhibition) exhibitManager.getExhibitionById(exhibitID);
        }
        exhibition.setStartSpace(startSpace);
        exhibition.setTitle(title);
        exhibition.setMode(exhibitMode);

        exhibitManager.updateExhibitionLanguages(exhibition, languages, defaultLanguage);

        if (exhibitMode.equals(ExhibitionModes.OFFLINE) && !customMessage.equals(ExhibitionModes.OFFLINE.getValue())) {

            exhibition.setCustomMessage(customMessage);
        }

        exhibition = (Exhibition) exhibitManager.storeExhibition(exhibition);
        attributes.addAllAttributes(addSuccessAttributes(exhibition));

        return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
    }
    
    /**
     * To create or update the default link images.
     * 
     * @param exhibitID - the ID of the exhibition to update
     * @param spaceLinkImage - the MultipartFile representing the space link image to be uploaded
     * @param moduleLinkImage - the MultipartFile representing the module link image to be uploaded
     * @param externalLinkImage - - the MultipartFile representing the external link image to be uploaded
     * @param attributes - - the RedirectAttributes object to add flash attributes
     * @return - a RedirectView to the exhibition configuration page
     * @throws IOException if an input or output error occurs
     */
    @RequestMapping(value = "/staff/exhibit/config/link/defaultImage", method = RequestMethod.POST)
    public RedirectView createOrUpdateLinkImage(HttpServletRequest request,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestParam(name="linkType", required=true) String linkType,
            RedirectAttributes attributes) throws IOException {
        Exhibition exhibition = (Exhibition) exhibitManager.getStartExhibition();;
        
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
            return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
        }
        
        exhibition = (Exhibition) exhibitManager.storeExhibition(exhibition);
        attributes.addAllAttributes(addSuccessAttributes(exhibition));

        return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
    }  
    
    /**
     * Method to return values indicating a successful operation
     * 
     * @param exhibition - the {@link Exhibition} object
     * @return map of attributes indicating a successful execution of the task
     */
    private Map<String, String> addSuccessAttributes(IExhibition exhibition) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("exhibitId", exhibition.getId());
        attributes.put("alertType", "success");
        attributes.put("message", "Successfully Saved!");
        attributes.put("showAlert", "true");
        return attributes;
    }
}
