package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.core.services.impl.FileManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;

@Controller
public class AddFileController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private FileManager fileManager;
    
    @RequestMapping(value = "/staff/files/add", method = RequestMethod.POST)
    public ResponseEntity<String> createFile( Model model, @ModelAttribute FileForm fileForm, @RequestParam("file") MultipartFile file,
            Principal principal) {
        byte[] fileBytes = null;
        String originalFileName = null;
        CreationReturnValue returnVal = null;
        if (file != null) {
            try {
                fileBytes = file.getBytes();
                originalFileName = file.getOriginalFilename();
                returnVal = fileManager.storeFile(fileBytes, originalFileName, fileForm.getFileName(),fileForm.getDescription());
              
            } catch (IOException e) {
                logger.error("Error occured while creating file", e);  
                return new ResponseEntity<String>("Could not create file", HttpStatus.BAD_REQUEST);
            }
            
        }      
        return new ResponseEntity<String>("File uploaded successfully", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/staff/files/add", method = RequestMethod.GET)
    public String showAddFile(Model model) {
        model.addAttribute("files", new FileForm());
        
        return "staff/files/add";
    }

}
