package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.services.impl.FileManager;

@Controller
public class FileController {

    @Autowired
    private FileManager fileManager;

    @RequestMapping(value = "/staff/files/{id}", method = RequestMethod.GET)
    public String getFile(Model model, @PathVariable String id) {
        IVSFile file = fileManager.getFileById(id);
        model.addAttribute("file", file);
        return "staff/files/file";
    }
}
