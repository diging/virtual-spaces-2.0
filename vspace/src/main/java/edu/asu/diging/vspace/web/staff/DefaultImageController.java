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

    public static final String API_DEFAULT_SPACE_IMAGE_PATH = "/api/image/default/link/space/";
    public static final String API_DEFAULT_MODULE_IMAGE_PATH = "/api/image/default/link/module/";
    public static final String API_DEFAULT_EXTERNAL_IMAGE_PATH = "/api/image/default/link/external/";
    public static final String API_DEFAULT_SPACE_IMAGE_STATUS = "/api/exhibition/default/link/image/status/";

    @Autowired
    private IImageService imageService;

    @Autowired
    private IExhibitionManager exhibitManager;

    @RequestMapping(value = API_DEFAULT_SPACE_IMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultSpaceImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage spaceImage = exhibition.getSpaceLinkDefaultImage();
        return getResponseWithDefaultHeaders(spaceImage);
    }

    @RequestMapping(value = API_DEFAULT_MODULE_IMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultModuleImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage moduleImage = exhibition.getModuleLinkDefaultImage();
        return getResponseWithDefaultHeaders(moduleImage);
    }

    @RequestMapping(value = API_DEFAULT_EXTERNAL_IMAGE_PATH, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDefaultExternalImage() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        IVSImage externalLinkImage = exhibition.getExternalLinkDefaultImage();
        return getResponseWithDefaultHeaders(externalLinkImage);
    }

    /**
     * Retrieves the status of default images for exhibition links
     * @return A JSON response containing flags indicating the availability of default images.
     */
    @RequestMapping(value = API_DEFAULT_SPACE_IMAGE_STATUS, method = RequestMethod.GET)
    public ResponseEntity<String> getDefaultImageStatus() {
        IExhibition exhibition = exhibitManager.getStartExhibition();
        exhibition.getSpaceLinkDefaultImage();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("defaultSpaceImageFlag", exhibition.getSpaceLinkDefaultImage() != null? true : false);
        jsonObj.addProperty("defaultModuleImageFlag", exhibition.getModuleLinkDefaultImage() != null? true : false);
        jsonObj.addProperty("defaultExternalLinkImageFlag", exhibition.getExternalLinkDefaultImage() != null? true : false);
        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);
    }

    private ResponseEntity<byte[]> getResponseWithDefaultHeaders(IVSImage image) {
        if(image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] imageContent = imageService.getImageContent(image);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(image.getFileType()));
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

}
