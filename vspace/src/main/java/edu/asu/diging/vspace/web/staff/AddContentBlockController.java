package edu.asu.diging.vspace.web.staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddContentBlockController {

    @RequestMapping(value="/staff/module/slide/content", method=RequestMethod.POST)
    public void addContentBlock(Model model) {
        
        System.out.println("inside content controller");
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