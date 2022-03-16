package edu.asu.diging.vspace.web.staff.api;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.web.staff.FileApiManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@RestController
public class FileApiController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private FileApiManager apiManager;
    
    @Autowired
    private IStorageEngine storage;
    
    @RequestMapping(value = "/api/file", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFiles(@PathVariable String id) {
        IVSImage image = imageRepo.findById(id).get();
        byte[] imageContent = null;
        try {
            imageContent = storage.getImageContent(image.getId(), image.getFilename());
        } catch (IOException e) {
            logger.error("Could not retrieve image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.parseMediaType(image.getFileType()));
         
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/file/create", method = RequestMethod.POST)
    public String createFile(Model model, @ModelAttribute FileForm fileForm, @RequestParam("file") MultipartFile file,
            Principal principal, @RequestParam(value = "imageId", required=false) String imageId, RedirectAttributes redirectAttrs) {
        byte[] fileBytes = null;
        String originalFileName = null;
        CreationReturnValue returnVal = null;
        if (file != null) {
            try {
                fileBytes = file.getBytes();
                originalFileName = file.getOriginalFilename();
                returnVal = apiManager.storeFile(fileBytes, originalFileName, fileForm.getFileName(),fileForm.getDescription());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        return "ok";
    }

}
