package edu.asu.diging.vspace.web.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value="/api/video/{id}")
    public ResponseEntity<byte[]> getVideo(@PathVariable String id, HttpServletResponse response,
            HttpServletRequest request) {
        IVSVideo video = videoRepo.findById(id).orElse(null);
        if (video == null) {
            logger.warn("Video not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] videoContent = null;
        try {
            videoContent = storage.getMediaContent(video.getId(), video.getFilename());
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Could not retrieve video.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = MediaResponseHeaders.create(video.getFileType());
        headers.remove(HttpHeaders.CACHE_CONTROL);
        headers.setContentLength(videoContent.length);
        return new ResponseEntity<>(videoContent, headers, HttpStatus.OK);
    } 
}
