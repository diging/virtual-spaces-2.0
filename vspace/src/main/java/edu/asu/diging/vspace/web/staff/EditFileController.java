package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.services.impl.FileManager;
import edu.asu.diging.vspace.web.staff.forms.FileForm;

@Controller
public class EditFileController {

    @Autowired
    private FileManager fileManager;

    @RequestMapping(value = "/staff/files/{fileId}/edit", method = RequestMethod.POST)
    public String updateFile(Model model, @PathVariable String fileId, @ModelAttribute FileForm fileForm) {
        IVSFile file = fileManager.editFile(fileId, fileForm.getFileName(), fileForm.getDescription());
        model.addAttribute("file", file);

        return "staff/files/file";
    }
    
    @RequestMapping(value = "/staff/files/{fileId}/edit", method = RequestMethod.GET)
    public String editFile(Model model, @PathVariable String fileId) {
        IVSFile file = fileManager.getFileById(fileId);
        model.addAttribute("file", file);
        return "staff/files/file";
    }

}
