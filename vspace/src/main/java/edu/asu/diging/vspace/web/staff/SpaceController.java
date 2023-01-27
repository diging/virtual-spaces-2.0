package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class SpaceController {

    public static final String STAFF_SPACE_PATH = "/staff/space/";

    public static final String API_IMAGE_PATH = "/api/image/";
    
    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private IStorageEngine storage;
    
    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISpaceDisplayManager spaceDisplayManager;

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;
    
    @Autowired
    private IImageService imageService;
    
    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(STAFF_SPACE_PATH+"{id}")
    public String showSpace(@PathVariable String id, Model model) throws IOException {
        
        
        ISpace space = spaceManager.getFullyLoadedSpace(id);
        model.addAttribute("linksOnThisSpace", spaceManager.getOutgoingLinks(id));
        model.addAttribute("linksToThisSpace",spaceManager.getIncomingLinks(id));
        model.addAttribute("space", space);
        model.addAttribute("spaceLinks", spaceLinkManager.getLinkDisplays(id));
        model.addAttribute("externalLinks", externalLinkManager.getLinkDisplays(id));
        model.addAttribute("moduleLinks", moduleLinkManager.getLinkDisplays(id));
        model.addAttribute("spaces", spaceManager.getAllSpaces());
        model.addAttribute("display", spaceDisplayManager.getBySpace(space));
        model.addAttribute("moduleList", moduleManager.getAllModules());
        return "staff/spaces/space";
    }

    @RequestMapping(value = "/staff/spaceLink/{spaceId}/spaces", method = RequestMethod.GET)
    public ResponseEntity<List<SpaceLink>> getSpaceLinksPresent(@PathVariable("spaceId") String spaceId) {
        List<SpaceLink> spaceLinkPresent = spaceManager.getIncomingLinks(spaceId);
        return new ResponseEntity<>(spaceLinkPresent, HttpStatus.OK);
    }
    
    @RequestMapping(value = STAFF_SPACE_PATH+"{id}/links", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> showSpaceLinks(@PathVariable String id, Model model) {
        Map<String,Object> responseData = new HashMap<String,Object>(); 
        
        
        responseData.put("spaceLinks", spaceLinkManager.getLinkDisplays(id));
       
        responseData.put("externalLinks", externalLinkManager.getLinkDisplays(id));
        responseData.put("moduleLinks", moduleLinkManager.getLinkDisplays(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
    
    @RequestMapping(value = STAFF_SPACE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getSpaceId() {
        IVSImage image = imageRepo.findById("IMG000000045").get();
        byte[] imageContent = null;
        
        try {
            imageContent = storage.getImageContent(image.getId(), image.getFilename());
            
        } catch (IOException e) {
            logger.error("Could not retrieve image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(image.getFileType()));

        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
        
        
    }
    
    
    
    
    
}
