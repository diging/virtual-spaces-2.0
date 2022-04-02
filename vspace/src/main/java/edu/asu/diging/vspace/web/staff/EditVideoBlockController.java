package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class EditVideoBlockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/video/{videoBlockId}", method = RequestMethod.POST)
    public ResponseEntity<String> editVideoBlock(@PathVariable("id") String slideId,
            @PathVariable("videoBlockId") String blockId, @PathVariable("moduleId") String moduleId,
            @RequestParam(value = "videoFile", required = false) MultipartFile file,
            @RequestParam(value = "url", required = false) String videoUrl,
            @RequestParam(value = "videoTitle", required = false) String videoTitle, Principal principal,
            RedirectAttributes attributes) throws IOException {

        IVideoBlock videoBlock = contentBlockManager.getVideoBlock(blockId);
        if (videoTitle != null && (videoUrl == null || file == null)) {
            contentBlockManager.updateVideoTitle(videoBlock, videoTitle);
        } else {
            byte[] video = null;
            String filename = null;
            if (file != null) {
                video = file.getBytes();
                filename = file.getOriginalFilename();
            }
            try {
                contentBlockManager.updateVideoBlock(videoBlock, video, (file != null) ? file.getSize() : null,
                        videoUrl, filename, videoTitle);
            } catch (VideoCouldNotBeStoredException e) {
                logger.error("Video block could not be stored, bad request.", e);
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode node = mapper.createObjectNode();
                node.put("errorMessage", "Video Content block cannot be stored.");
                return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}