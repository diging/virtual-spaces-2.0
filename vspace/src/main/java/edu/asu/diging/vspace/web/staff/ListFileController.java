
package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.FileManager;

@Controller
public class ListFileController {
        
    @Autowired
    private FileManager fileManager;
    
    @RequestMapping(value = "/staff/files/list", method = RequestMethod.GET)
    public String getFilesList(Model model, @RequestParam(value = "filesPagenum", required = false, defaultValue = "1") String filesPagenum) {
               
        Page<VSFile> filesPage = fileManager.getAllFiles(Integer.parseInt(filesPagenum));
        model.addAttribute("filesCurrentPageNumber", Integer.parseInt(filesPagenum));
        model.addAttribute("filesTotalPages", filesPage.getTotalPages());
        model.addAttribute("files", filesPage.getContent());
        model.addAttribute("filesCount", filesPage.getTotalElements());
        return "staff/files/filelist";
    }
    
   
  

}
