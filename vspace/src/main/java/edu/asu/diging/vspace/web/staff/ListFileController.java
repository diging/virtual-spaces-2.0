
package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.FileManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;

@Controller
public class ListFileController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private FileManager fileManager;
    
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
    
   
  

}
