package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

@Controller
public class ExhibitionConfigurationController {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private SpaceManager spaceManager;

    @Autowired
    private IExhibitionManager exhibitManager;

    @Autowired
    private ExhibitionFactory exhibitFactory;

    @RequestMapping("/staff/exhibit/config")
    public String showExhibitions(Model model) {
        // for now we assume there is just one exhibition
        IExhibition exhibition = exhibitManager.getStartExhibition();
        if(exhibition!=null) {
            model.addAttribute("exhibition", exhibition);
        } else {
            model.addAttribute("exhibition", new Exhibition());
        }
        model.addAttribute("exhibitionModes", Arrays.asList(ExhibitionModes.values()));
        model.addAttribute("spacesList", spaceRepo.findAll());
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
        RedirectAttributes attributes) throws IOException {

        ISpace startSpace = spaceManager.getSpace(spaceID);

        Exhibition exhibition;
        if(exhibitID==null || exhibitID.isEmpty()) {
            exhibition = (Exhibition) exhibitFactory.createExhibition();
        } else {
            exhibition = (Exhibition) exhibitManager.getExhibitionById(exhibitID);
        }
        exhibition.setStartSpace(startSpace);
        exhibition.setTitle(title);
        exhibition.setMode(exhibitMode);
        if(exhibitMode.equals(ExhibitionModes.OFFLINE) && !customMessage.equals(ExhibitionModes.OFFLINE.getValue())) {
            exhibition.setCustomMessage(customMessage);
        }
        exhibition = (Exhibition) exhibitManager.storeExhibition(exhibition);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");
        return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
    }
}
