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

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class SpaceController {

    public static final String STAFF_SPACE_PATH = "/staff/space/";

    public static final String API_DEFAULT_SPACEIMAGE_PATH = "/api/defaultSpaceImage/";

    public static final String API_DEFAULT_MODULEIMAGE_PATH = "/api/defaultModuleImage/";

    public static final String API_DEFAULT_EXTERNALIMAGE_PATH = "/api/defaultExternalLinkImage/";
    
    public static final String API_DEFAULT_SPACEIMAGE = "/api/getdefaultImage/";

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
    private IExhibitionManager exhibitManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(STAFF_SPACE_PATH + "{id}")
    public String showSpace(@PathVariable String id, Model model) throws IOException {

        ISpace space = spaceManager.getFullyLoadedSpace(id);
        model.addAttribute("linksOnThisSpace", spaceManager.getOutgoingLinks(id));
        model.addAttribute("linksToThisSpace", spaceManager.getIncomingLinks(id));
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

    @RequestMapping(value = STAFF_SPACE_PATH
            + "{id}/links", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> showSpaceLinks(@PathVariable String id, Model model) {
        Map<String, Object> responseData = new HashMap<String, Object>();

        responseData.put("spaceLinks", spaceLinkManager.getLinkDisplays(id));

        responseData.put("externalLinks", externalLinkManager.getLinkDisplays(id));
        responseData.put("moduleLinks", moduleLinkManager.getLinkDisplays(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @RequestMapping(value = API_DEFAULT_SPACEIMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getSpaceId() {
        IExhibition exhibition = exhibitManager.getStartExhibition();

        IVSImage spaceImage = exhibition.getSpacelinkImage();
        if (spaceImage == null) {
            return null;
        }
        byte[] imageContent = null;

        try {

            imageContent = storage.getImageContent(spaceImage.getId(), spaceImage.getFilename());

        } catch (IOException e) {
            logger.error("Could not retrieve image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(spaceImage.getFileType()));

        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);

    }

    @RequestMapping(value = API_DEFAULT_MODULEIMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultModuleImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage moduleImage = exhibition.getModulelinkImage();
        if (moduleImage == null) {
            return null;
        }
        byte[] imageContent = null;

        try {

            imageContent = storage.getImageContent(moduleImage.getId(), moduleImage.getFilename());

        } catch (IOException e) {
            logger.error("Could not retrieve default Module image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(moduleImage.getFileType()));
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);

    }
    
    @RequestMapping(value = API_DEFAULT_EXTERNALIMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultExternalImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage externalLinkImage = exhibition.getExternallinkImage();
        if (externalLinkImage == null) {
            return null;
        }
        byte[] imageContent = null;

        try {

            imageContent = storage.getImageContent(externalLinkImage.getId(), externalLinkImage.getFilename());

        } catch (IOException e) {
            logger.error("Could not retrieve default External Link image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(externalLinkImage.getFileType()));
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);

    }
    
    @RequestMapping(value = API_DEFAULT_SPACEIMAGE, method = RequestMethod.GET)
    public ResponseEntity<String> getDefaultImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage defaultSpaceImage = exhibition.getSpacelinkImage();
        IVSImage defaultModuleImage = exhibition.getModulelinkImage();
        IVSImage defaultExternalLinkImage = exhibition.getExternallinkImage();
        
        String defaultSpaceImageFlag = "";
        String defaultModuleImageFlag = "";
        String defaultExternalLinkImageFlag = "";
        if (defaultSpaceImage == null) {
            defaultSpaceImageFlag = "NO_IMAGE";
        }else {
            defaultSpaceImageFlag = "IMAGE";
        }
        
        if (defaultModuleImage == null) {
            defaultModuleImageFlag = "NO_IMAGE";
        }else {
            defaultModuleImageFlag = "IMAGE";
        }
        
        if (defaultExternalLinkImage == null) {
            defaultExternalLinkImageFlag = "NO_IMAGE";
        }else {
            defaultExternalLinkImageFlag = "IMAGE";
        }
        
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("defaultSpaceImageFlag",defaultSpaceImageFlag );
        jsonObj.addProperty("defaultModuleImageFlag",defaultModuleImageFlag );
        jsonObj.addProperty("defaultExternalLinkImageFlag",defaultExternalLinkImageFlag );
        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);

    }

}
