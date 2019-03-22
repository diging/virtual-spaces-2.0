package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AddContentBlockController {

	@Autowired
	private IContentBlockManager contentBlockManager;

	@RequestMapping(value = "/staff/module/slide/{id}/textcontent", method = RequestMethod.POST)
	public ResponseEntity<String> addTextBlock(@PathVariable("id") String slideId, @RequestParam("content") String content,
			@RequestParam("type") String type) throws IOException {

		System.out.print("inside controller");
		System.out.println(content);
		System.out.println(slideId);

		contentBlockManager.createTextBlock(slideId, content);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/staff/module/slide/{id}/imagecontent", method = RequestMethod.POST)
	public String addImageBlock(@PathVariable("id") String slideId, @RequestParam("file") MultipartFile file,
			Principal principal, RedirectAttributes attributes) throws IOException {

		System.out.print("inside Image controller");

		byte[] bgImage = null;
		String filename = null;
		if (file.isEmpty() || file.equals(null)) {
			attributes.addAttribute("alertType", "danger");
			attributes.addAttribute("showAlert", "true");
			attributes.addAttribute("message", "Please select a background image.");
			//return "redirect:/staff/space/{id}";
			return "redirect:/staff/module/slide/{id}";

		} else if (file != null) {
			bgImage = file.getBytes();
			filename = file.getOriginalFilename();
		}

		contentBlockManager.createImageBlock(slideId, bgImage, filename);

		return "redirect:/staff/module/slide/{id}";

	}
}