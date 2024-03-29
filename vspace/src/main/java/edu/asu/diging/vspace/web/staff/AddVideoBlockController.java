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
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

@Controller
public class AddVideoBlockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/video", method = RequestMethod.POST)
    public ResponseEntity<String> addVideoBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestParam(required = false) MultipartFile videoFile,
            @RequestParam(required = false) String url, @RequestParam(required = false) String videoTitle,
            Principal principal, RedirectAttributes attributes) throws IOException {

        Integer contentOrder = contentBlockManager.findMaxContentOrder(slideId);
        contentOrder = contentOrder == null ? 0 : contentOrder + 1;
        String videoId;
        try {
            if (videoFile == null && url.isEmpty()) {
                throw new VideoCouldNotBeStoredException();
            }
            byte[] video = null;
            String fileName = null;
            if (videoFile != null) {
                video = videoFile.getBytes();
                fileName = videoFile.getOriginalFilename();
            }
            CreationReturnValue videoBlockValue = contentBlockManager.createVideoBlock(slideId, video,
                    (videoFile != null) ? videoFile.getSize() : null, fileName, url, contentOrder, videoTitle);
            videoId = videoBlockValue.getElement().getId();
        } catch (VideoCouldNotBeStoredException e) {
            logger.warn("Video block could not be stored, bad request.", e);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "Video Content block cannot be stored.");
            return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(videoId, HttpStatus.OK);
    }

}
