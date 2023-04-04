package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Controller
public class DefaultImageController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String API_DEFAULT_SPACEIMAGE_PATH = "/api/defaultSpaceImage/";
    public static final String API_DEFAULT_MODULEIMAGE_PATH = "/api/defaultModuleImage/";
    public static final String API_DEFAULT_EXTERNALIMAGE_PATH = "/api/defaultExternalLinkImage/";
    public static final String API_DEFAULT_SPACEIMAGE = "/api/getdefaultImage/";
    
    @Autowired
    private IStorageEngine storage;
    
    @Autowired
    private IExhibitionManager exhibitManager;
    
    @RequestMapping(value = API_DEFAULT_SPACEIMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getSpaceId() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage spaceImage = exhibition.getSpacelinkDefaultImage();
        if (spaceImage == null) {
            return null;
        }
        byte[] imageContent = null;
        try {
            imageContent = storage.getMediaContent(spaceImage.getId(), spaceImage.getFilename());
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
        IVSImage moduleImage = exhibition.getModulelinkDefaultImage();
        if (moduleImage == null) {
            return null;
        }
        byte[] imageContent = null;

        try {

            imageContent = storage.getMediaContent(moduleImage.getId(), moduleImage.getFilename());

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
        IVSImage externalLinkImage = exhibition.getExternallinkDefaultImage();
        if (externalLinkImage == null) {
            return null;
        }
        byte[] imageContent = null;

        try {

            imageContent = storage.getMediaContent(externalLinkImage.getId(), externalLinkImage.getFilename());

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
        List<IVSImage> defaultImageList = new ArrayList<>();
        defaultImageList = exhibitManager.getDefaultImage();
        IVSImage defaultSpaceImage = defaultImageList.get(0);
        IVSImage defaultModuleImage = defaultImageList.get(1);
        IVSImage defaultExternalLinkImage = defaultImageList.get(2);

        boolean defaultSpaceImageFlag = false;
        boolean defaultModuleImageFlag = false;
        boolean defaultExternalLinkImageFlag = false;
        if (defaultSpaceImage != null) {
            defaultSpaceImageFlag = true;
        }
        if (defaultModuleImage != null) {
            defaultModuleImageFlag = true;
        }
        if (defaultExternalLinkImage != null) {
            defaultExternalLinkImageFlag = true;
        }
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("defaultSpaceImageFlag", defaultSpaceImageFlag);
        jsonObj.addProperty("defaultModuleImageFlag", defaultModuleImageFlag);
        jsonObj.addProperty("defaultExternalLinkImageFlag", defaultExternalLinkImageFlag);
        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);

    }
    
    @RequestMapping(value = API_DEFAULT_EXTERNALIMAGE_PATH+"test", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultImages() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        List<IVSImage> defaultImageList = new ArrayList<>();
        defaultImageList = exhibitManager.getDefaultImage();
        byte[] imageContent = null;
        for (IVSImage image : defaultImageList) {
            System.out.println(image);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
        

    }

}
