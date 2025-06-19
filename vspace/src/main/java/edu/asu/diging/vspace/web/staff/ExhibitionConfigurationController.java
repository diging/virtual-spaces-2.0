package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import edu.asu.diging.vspace.web.staff.forms.ExhibitionConfigurationForm;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;

@Controller
public class ExhibitionConfigurationController {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IExhibitionManager exhibitionManager;

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

        IExhibition exhibition = exhibitionManager.getStartExhibition();
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
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sortedLanguageList = exhibitionLanguageConfig.getExhibitionLanguageList().stream()
                .map(rawMap -> (Map<String, Object>) rawMap)  // Cast raw Map to Map<String, Object>
                .sorted((lang1, lang2) -> {
                    String label1 = (String) lang1.get("label");
                    String label2 = (String) lang2.get("label");
                    return label1.compareToIgnoreCase(label2);
                })
                .collect(Collectors.toList());
                
        model.addAttribute("languageList", sortedLanguageList);
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
            @RequestParam("spaceParam") String spaceID,
            @Valid @ModelAttribute("exhibitionConfigurationForm") ExhibitionConfigurationForm exhibitionConfigForm,
            BindingResult result,           
            RedirectAttributes attributes) throws IOException {
        if(result.hasErrors()) {
            attributes.addAttribute("showAlert", true);
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("message", result.getFieldError().getDefaultMessage());
            return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
        }
        ExhibitionModes exhibitMode = exhibitionConfigForm.getExhibitionMode();
        List<String> languages = exhibitionConfigForm.getExhibitLanguage();
        String defaultLanguage = exhibitionConfigForm.getDefaultExhibitLanguage();
        String customMessage = exhibitionConfigForm.getCustomMessage();
        IExhibition exhibition;

        if (exhibitID == null || exhibitID.isEmpty()) {
            exhibition = exhibitFactory.createExhibition();
        } else {
            exhibition = exhibitionManager.getExhibitionById(exhibitID);
        }
        exhibition.setStartSpace(spaceManager.getSpace(spaceID));
        exhibition.setTitle(exhibitionConfigForm.getTitle());
        exhibition.setMode(exhibitMode);

        exhibitionManager.updateExhibitionLanguages(exhibition, languages, defaultLanguage);

        if (exhibitMode.equals(ExhibitionModes.OFFLINE) && !customMessage.equals(ExhibitionModes.OFFLINE.getValue())) {

            exhibition.setCustomMessage(customMessage);
        }

        exhibition = exhibitionManager.storeExhibition(exhibition);
        attributes.addAttribute("exhibitId", exhibition.getId());
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");

        return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
    }
    
}
