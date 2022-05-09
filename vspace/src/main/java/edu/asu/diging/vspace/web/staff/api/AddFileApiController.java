package edu.asu.diging.vspace.web.staff.api;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.core.services.impl.FileApiManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;

@Controller
public class AddFileApiController {
    
    @Autowired
    private FileApiManager fileManager;
    
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
                e.printStackTrace();
            }
            
        }
        
        return "redirect:/staff/files/list";
    }
    
    @RequestMapping(value = "/staff/files/add", method = RequestMethod.GET)
    public String showAddFile(Model model) {
        model.addAttribute("files", new FileForm());
        
        return "staff/files/add";
    }

}
