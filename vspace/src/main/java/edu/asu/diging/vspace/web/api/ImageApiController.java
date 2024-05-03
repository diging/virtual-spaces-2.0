package edu.asu.diging.vspace.web.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;

@RestController
public class ImageApiController {
    
    public static final String API_IMAGE_PATH = "/api/image/";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    @RequestMapping(API_IMAGE_PATH + "{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        IVSImage image = imageRepo.findById(id).get();
        byte[] imageContent = null;
        try {
            imageContent = storage.getMediaContent(image.getId(), image.getFilename());
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
