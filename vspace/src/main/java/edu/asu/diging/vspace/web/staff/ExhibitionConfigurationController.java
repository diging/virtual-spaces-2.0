package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.javers.common.collections.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
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
    
    private byte[]  spaceImage;

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
            Principal principal,
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
        System.out.println("********************************"+spaceImage);
        exhibition.setSpacelinkImage(spaceImage);
        
        
        if(exhibitMode.equals(ExhibitionModes.OFFLINE) && !customMessage.equals(ExhibitionModes.OFFLINE.getValue())) {
            exhibition.setCustomMessage(customMessage);
        }
        exhibition = (Exhibition) exhibitManager.storeExhibition(exhibition);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");
        
        
        return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
    }
    
    @RequestMapping(value = "/staff/exhibit/config/images", method = RequestMethod.POST)
    public RedirectView updateSpace( HttpServletRequest request, Model model, 
    		@RequestParam("externalLinkImage") MultipartFile externalLinkImage,
    		@RequestParam("spacelinkImage")  MultipartFile spacelinkImage,
    		@RequestParam("moduleLinkImage")  MultipartFile moduleLinkImage,
            Principal principal, RedirectAttributes attributes) throws IOException {
    	
    	
        String spaceLinkFilename = null;
        if (spacelinkImage != null) {
        	spaceImage = spacelinkImage.getBytes();
        	spaceLinkFilename = spacelinkImage.getOriginalFilename();
            
        }
        
        byte[] moduleImage = null;
        String moduleLinkFilename = null;
        if (spacelinkImage != null) {
        	moduleImage = moduleLinkImage.getBytes();
        	moduleLinkFilename = moduleLinkImage.getOriginalFilename();
            
        }
    	
        byte[] externalImage = null;
        String externalLinkFilename = null;
        if (externalLinkImage != null) {
        	externalImage = externalLinkImage.getBytes();
        	externalLinkFilename = externalLinkImage.getOriginalFilename();
            
        }
       
        exhibitManager.storeDefaultImage(spaceImage, spaceLinkFilename);
        
        return new RedirectView(request.getContextPath() + "/staff/exhibit/config");
    }
    
    
    
    
    
   
    
    
    
    
    
    
}
