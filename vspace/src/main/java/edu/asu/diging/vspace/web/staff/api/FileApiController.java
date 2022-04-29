
package edu.asu.diging.vspace.web.staff.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.web.staff.FileApiManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

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
        return "staff/files/file";
    }
    
    @RequestMapping(value = "/staff/files/list", method = RequestMethod.GET)
    public String getFilesList(Model model) {
        List<VSFile> files = fileManager.getAllFiles();
        model.addAttribute("files", files);
        return "staff/files/filelist";
    }
    
    @RequestMapping(value = "/staff/files/add", method = RequestMethod.GET)
    public String showAddFile(Model model) {
        model.addAttribute("files", new FileForm());
        
        return "staff/files/add";
    }
    
    @RequestMapping(value = "/staff/files/add", method = RequestMethod.POST)
    public String createFile(Model model, @ModelAttribute FileForm fileForm, @RequestParam("file") MultipartFile file,
            Principal principal, RedirectAttributes redirectAttrs) {
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
        
        return "redirect:/staff/files/list";
    }
    
    @RequestMapping(value = "/staff/files/edit/{fileId}", method = RequestMethod.POST)
    public String editFile(Model model, @PathVariable String fileId, @ModelAttribute FileForm fileForm) {
        String fileName = fileForm.getFileName();
        String description = fileForm.getDescription();
        IVSFile file = fileManager.editFile(fileId, fileName, description);
        String id = file.getId();
        return "redirect:/staff/files/"+id;
    }
    
    @RequestMapping(value = "/staff/files/download/{fileId}", method = RequestMethod.GET)
    public String downloadFile(Model model, @PathVariable String fileId) {
        IVSFile file = fileManager.downloadFile(fileId);
        String id = file.getId();
        return "redirect:/staff/files/"+id;
    }

}
