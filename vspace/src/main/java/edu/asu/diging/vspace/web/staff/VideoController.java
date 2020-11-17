package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.model.IVSVideo;

@Controller
public class VideoController {
    
    @Autowired
    private VideoRepository videoRepo;

    @RequestMapping("/staff/display/video/{id}")
    public String showImage(@PathVariable String id, Model model) {
        IVSVideo video = videoRepo.findById(id).get();
        model.addAttribute("video", video);
        return "staff/images/image";
    }
}