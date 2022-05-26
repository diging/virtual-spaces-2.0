package edu.asu.diging.vspace.web.staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ListDownloadsController {

    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model) {
        
        return "staff/downloads/downloadList";
    }
}
