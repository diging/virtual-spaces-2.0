package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.services.ISnapshotManager;

@Controller
public class DownloadsController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
       
    @Autowired
    private ISnapshotManager snapshotManager;
    
    @RequestMapping("/staff/snapshots/list")
    public String listDownloads(Model model, @RequestParam(value = "downloadsPagenum", required = false, defaultValue = "1") String downloadsPagenum) {
        Integer pageNum = 1;
        try {           
            pageNum =  Integer.parseInt(downloadsPagenum);
        } catch(NumberFormatException e) {
            logger.error("Invalid page number", e);
        }        
        Page<ExhibitionSnapshot> downloadsPage = snapshotManager.getAllExhibitionSnapshots(pageNum);
        model.addAttribute("downloadsList" , downloadsPage.getContent());
        model.addAttribute("downloadsCurrentPageNumber", Integer.parseInt(downloadsPagenum));
        model.addAttribute("downloadsTotalPages", downloadsPage.getTotalPages());
        model.addAttribute("downloadsCount", downloadsPage.getTotalElements());
        
        return "exhibition/downloads/downloadList";
    }   
}
