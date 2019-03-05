package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.factory.ISlideFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class AddSlideController {
       
    @Autowired
    private ISlideFactory slideFactory;
    
    @Autowired
    private ISlideManager slideManager;
    
    private String modId;
     
    @RequestMapping(value="/staff/module/{id}/slide/add", method=RequestMethod.GET)
    public String showAddSlide(@PathVariable("id") String moduleId, Model model) {
        model.addAttribute("slide", new SlideForm());
        modId = moduleId;
        return "staff/module/slide/add";
    }
    
    @RequestMapping(value="/module/slide/add", method=RequestMethod.POST)
    public String addSlide(Model model, @ModelAttribute SlideForm slideForm, Principal principal) {
        
        ISlide slide = slideFactory.createSlide(slideForm);
        slideManager.storeSlide(slide);
        System.out.println("heloo------");
        return "redirect:/staff/module/{modId}";
    }
    

}



//@RequestMapping(value = "/staff/module/{id}/slide/", method = RequestMethod.POST)
//public String addSlide(@PathVariable("id") String moduleId, @RequestParam("slideTitle") String title, @RequestParam("slideDescription") String description, Model model, @RequestParam("file") MultipartFile file,
//      Principal principal, RedirectAttributes attributes) throws IOException, ModuleDoesNotExistException {
//  
//  ISlide slide = moduleManager.createSlide(moduleId,title,description);
//  byte[] image = null;
//  String filename = null;        
//  if (file.isEmpty() || file.equals(null)) {
//      attributes.addAttribute("alertType", "danger");
//      attributes.addAttribute("showAlert", "true");
//      attributes.addAttribute("message", "Please select an image for the Slide.");
//      return "redirect:/staff/module/{id}";
//
//  } else if (file != null) {
//      image = file.getBytes();
//      filename = file.getOriginalFilename();
//  }
//  moduleManager.storeSlide(slide, image, filename);
//  return "redirect:/staff/module/{id}";
//}