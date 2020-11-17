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

import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSVideo;

@RestController
public class VideoApiController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private VideoRepository videoRepo;
    
    @Autowired
    private IStorageEngine storage;
    
    @RequestMapping("/api/video/{id}")
    public ResponseEntity<byte[]> getVideo(@PathVariable String id) {
        System.out.println("Getting hitts!!!!!!");
        IVSVideo video = videoRepo.findById(id).get();
        byte[] videoContent = null;
        try {
            videoContent = storage.getVideoContent(video.getId(), video.getFilename());
        } catch (IOException e) {
            logger.error("Could not retrieve image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(video.getFileType()));
        headers.set("X-Frame-Options", "ALLOW");
         
        return new ResponseEntity<>(videoContent, headers, HttpStatus.OK);
    }
}
