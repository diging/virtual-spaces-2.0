package edu.asu.diging.vspace.web.staff;

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

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class DefaultImageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String API_DEFAULT_SPACE_IMAGE_PATH = "/api/image/default/link/space/";
    public static final String API_DEFAULT_MODULEIMAGE_PATH = "/api/image/default/link/module/";
    public static final String API_DEFAULT_EXTERNALIMAGE_PATH = "/api/image/default/link/externalLink/";
    public static final String API_DEFAULT_SPACE_IMAGE_DETAILS = "/api/exhibition/default/link/imageDetails/";

    @Autowired
    private IImageService imageService;

    @Autowired
    private IExhibitionManager exhibitManager;

    @RequestMapping(value = API_DEFAULT_SPACE_IMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultSpaceImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage spaceImage = exhibition.getSpacelinkDefaultImage();
        return getResponseWithDefaultHeaders(spaceImage);
    }

    @RequestMapping(value = API_DEFAULT_MODULEIMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultModuleImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage moduleImage = exhibition.getModulelinkDefaultImage();
        return getResponseWithDefaultHeaders(moduleImage);
    }

    @RequestMapping(value = API_DEFAULT_EXTERNALIMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultExternalImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage externalLinkImage = exhibition.getExternallinkDefaultImage();
        return getResponseWithDefaultHeaders(externalLinkImage);
    }

    @RequestMapping(value = API_DEFAULT_SPACE_IMAGE_DETAILS, method = RequestMethod.GET)
    public ResponseEntity<String> getDefaultImageDetails() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage defaultSpaceImage = exhibition.getSpacelinkDefaultImage();
        IVSImage defaultModuleImage = exhibition.getModulelinkDefaultImage();
        IVSImage defaultExternalLinkImage = exhibition.getExternallinkDefaultImage();

        boolean defaultSpaceImageFlag = defaultSpaceImage != null? true : false;
        boolean defaultModuleImageFlag = defaultModuleImage != null? true : false;
        boolean defaultExternalLinkImageFlag = defaultExternalLinkImage != null? true : false;

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("defaultSpaceImageFlag", defaultSpaceImageFlag);
        jsonObj.addProperty("defaultModuleImageFlag", defaultModuleImageFlag);
        jsonObj.addProperty("defaultExternalLinkImageFlag", defaultExternalLinkImageFlag);
        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);
    }

    private ResponseEntity<byte[]> getResponseWithDefaultHeaders(IVSImage image) {
    	byte[] imageContent = null;
        if(image == null) {
            return null;
        }
        imageContent = imageService.getImageContent(image);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(image.getFileType()));
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

}
