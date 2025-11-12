package edu.asu.diging.vspace.web.staff;

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
public class DefaultImageApiController {

    public static final String API_DEFAULT_SPACE_IMAGE_PATH = "/api/image/default/link/space/";
    public static final String API_DEFAULT_MODULE_IMAGE_PATH = "/api/image/default/link/module/";
    public static final String API_DEFAULT_EXTERNAL_IMAGE_PATH = "/api/image/default/link/external/";
    public static final String API_DEFAULT_SPACE_IMAGE_STATUS = "/api/exhibition/default/link/image/status/";
    public static final String API_DEFAULT_IMAGE_DISABLE_STATUS = "/api/exhibition/default/link/image/disableStatus/";

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
     * Retrieves the status of default images for exhibition links.
     * Returns true only if the image exists AND is not disabled.
     * @return A JSON response containing flags indicating the availability of default images.
     */
    @RequestMapping(value = API_DEFAULT_SPACE_IMAGE_STATUS, method = RequestMethod.GET)
    public ResponseEntity<String> getDefaultImageStatus() {
        IExhibition exhibition = exhibitManager.getStartExhibition();

        JsonObject jsonObj = new JsonObject();

        // Check if image exists and is not disabled
        IVSImage spaceImage = exhibition.getSpaceLinkDefaultImage();
        jsonObj.addProperty("defaultSpaceImageFlag",
            spaceImage != null && !spaceImage.getDisableFlag());

        IVSImage moduleImage = exhibition.getModuleLinkDefaultImage();
        jsonObj.addProperty("defaultModuleImageFlag",
            moduleImage != null && !moduleImage.getDisableFlag());

        IVSImage externalImage = exhibition.getExternalLinkDefaultImage();
        jsonObj.addProperty("defaultExternalLinkImageFlag",
            externalImage != null && !externalImage.getDisableFlag());

        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);
    }
    
    /**
     * Retrieves the disable status of default images for exhibition links.
     * Returns false if image is null (treated as not disabled since it doesn't exist).
     * @return A JSON response containing flags indicating whether default images are disabled.
     */
    @RequestMapping(value = API_DEFAULT_IMAGE_DISABLE_STATUS, method = RequestMethod.GET)
    public ResponseEntity<String> getDefaultImageDisableStatus() {
        IExhibition exhibition = exhibitManager.getStartExhibition();

        JsonObject jsonObj = new JsonObject();

        // Return false if image is null (not disabled because it doesn't exist)
        IVSImage spaceImage = exhibition.getSpaceLinkDefaultImage();
        jsonObj.addProperty("defaultSpaceImageDisableFlag",
            spaceImage != null && spaceImage.getDisableFlag());

        IVSImage moduleImage = exhibition.getModuleLinkDefaultImage();
        jsonObj.addProperty("defaultModuleImageDisableFlag",
            moduleImage != null && moduleImage.getDisableFlag());

        IVSImage externalImage = exhibition.getExternalLinkDefaultImage();
        jsonObj.addProperty("defaultExternalImageDisableFlag",
            externalImage != null && externalImage.getDisableFlag());

        return new ResponseEntity<>(jsonObj.toString(), HttpStatus.OK);
    }

    /**
     * Helper method to create response with appropriate headers for image content.
     * Returns 404 if image is null or disabled.
     *
     * @param image - the image to serve
     * @return ResponseEntity with image bytes and headers, or 404 if unavailable
     */
    private ResponseEntity<byte[]> getResponseWithDefaultHeaders(IVSImage image) {
        if(image == null || image.getDisableFlag() == true) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] imageContent = imageService.getImageContent(image);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(image.getFileType()));
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    
}
