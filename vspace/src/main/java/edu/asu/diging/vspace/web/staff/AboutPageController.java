package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;

@Controller
public class AboutPageController {
	
	 @RequestMapping("/staff/exhibit/aboutpage")
	    public String showAboutPage(Model model) {
		 model.addAttribute("exhiAbtPage", new ExhibitionAboutPage());
	        return "staff/exhibit/aboutPage";
	    }

	 
	 @RequestMapping(value = "/staff/exhibit/aboutpage", method = RequestMethod.POST)
	    public RedirectView createOrUpdateExhibition(HttpServletRequest request,
	            @RequestParam(required = false, name = "exhabtID") String exhabtID,
	            @RequestParam("title") String title,
	            @RequestParam("exhibitMode") List<String>aboutPageTextList,
	            RedirectAttributes attributes) throws IOException {
		 
		 return new RedirectView(request.getContextPath() + "/staff/exhibit/aboutpage");
	 }
	 
}
