
package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.FileManager;

@Controller
public class ListFileController {
        
    @Autowired
    private FileManager fileManager;
    
    @RequestMapping(value = "/staff/files/list", method = RequestMethod.GET)
    public String getFilesList(Model model) {
        List<VSFile> files = fileManager.getAllFiles();
        model.addAttribute("files", files);
        return "staff/files/filelist";
    }
    
   
  

}
