package edu.asu.diging.vspace.web.staff.api;

import java.io.IOException;
import java.security.Principal;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.web.staff.FileApiManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;

@Controller
public class FileApiController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private FileApiManager fileManager;
    
    @Autowired
    private IStorageEngine storage;
    
    @RequestMapping(value = "/staff/files/{id}", method = RequestMethod.GET)
    public String getFile(Model model, @PathVariable String id) {
        IVSFile file = fileManager.getFileById(id);
        model.addAttribute("file", file);
        return "staff/file/filedetails";
    }
    
    @RequestMapping(value = "/staff/files/list", method = RequestMethod.GET)
    public String getFilesList(Model model) {
        List<VSFile> files = fileManager.getAllFiles();
        model.addAttribute("files", files);
        return "staff/files/filelist";
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
                returnVal = fileManager.storeFile(fileBytes, originalFileName, fileForm.getFileName(),fileForm.getDescription());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        return "ok";
    }

}
