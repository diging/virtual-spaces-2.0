package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;   
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AddContentBlockController {
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @RequestMapping(value="/staff/module/slide/{id}/content", method=RequestMethod.POST)
    public String addContentBlock(@PathVariable("id") String slideId, @RequestParam("content") String content, @RequestParam("type") String type) throws IOException {
        
        System.out.print("inside controller");
        System.out.println(content);
        System.out.println(slideId);
        if(type.equals("String"))
            contentBlockManager.createTextBlock(slideId, content);
        else
            contentBlockManager.createImageBlock(slideId, content);
        
        return "redirect:content";
            
    }
}








//@RequestMapping(value = "/staff/module/{id}/slide/", method = RequestMethod.POST)
//public String addSlide(@PathVariable("id") String moduleId, @RequestParam("slideTitle") String title, @RequestParam("slideDescription") String description, Model model, @RequestParam("file") MultipartFile file,
//    Principal principal, RedirectAttributes attributes) throws IOException, ModuleDoesNotExistException {
//
//ISlide slide = moduleManager.createSlide(moduleId,title,description);
//byte[] image = null;
//String filename = null;        
//if (file.isEmpty() || file.equals(null)) {
//    attributes.addAttribute("alertType", "danger");
//    attributes.addAttribute("showAlert", "true");
//    attributes.addAttribute("message", "Please select an image for the Slide.");
//    return "redirect:/staff/module/{id}";
//
//} else if (file != null) {
//    image = file.getBytes();
//    filename = file.getOriginalFilename();
//}
//moduleManager.storeSlide(slide, image, filename);
//return "redirect:/staff/module/{id}";
//}