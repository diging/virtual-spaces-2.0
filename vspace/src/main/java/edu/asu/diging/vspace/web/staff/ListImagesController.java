package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.model.impl.VSImage;

@Controller
public class ListImagesController {
	
	@Autowired
	private ImageRepository imageRepo;
	
	@RequestMapping("/staff/images/list/{page}")
	public String listSpaces(@PathVariable String page, Model model) {
		int pageSize = 10; //need to move to a constant file
		long totalPages = imageRepo.count();
		Pageable sortedByName = PageRequest.of(Integer.parseInt(page), pageSize, Sort.by("filename"));
		Page<VSImage> images = imageRepo.findAll(sortedByName);
		model.addAttribute("images", images.getContent());
		model.addAttribute("totalpages", totalPages);
		return "staff/images/list";
	}
}
