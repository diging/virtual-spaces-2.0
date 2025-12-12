package edu.asu.diging.vspace.web.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSVideo;

@RestController
public class VideoApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storage;

    @RequestMapping(value="/api/video/{id}")
    public ResponseEntity<byte[]> getVideo(@PathVariable String id, HttpServletResponse response,
            HttpServletRequest request) {
        IVSVideo video = null;
        byte[] videoContent = null;
        try {
            video = videoRepo.findById(id).get();
            videoContent = storage.getMediaContent(video.getId(), video.getFilename());
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Could not retrieve video.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", video.getFileType())
                .header("Content-Length", String.valueOf(videoContent.length)).body(videoContent);
    } 
}
