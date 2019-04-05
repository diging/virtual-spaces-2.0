package edu.asu.diging.vspace.web.staff;

import java.util.HashMap;
import java.util.Map;

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
import edu.asu.diging.vspace.core.model.Constants;
import edu.asu.diging.vspace.core.model.SortWithField;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class ListImagesController {
	
	@Autowired
	private ImageRepository imageRepo;
	
    @Autowired
    private IImageService imageService;
    
	@RequestMapping("/staff/images/list/{page}")
	public String listSpaces(@PathVariable String page, Model model) {
		long totalPages = imageService.getTotalPages(imageRepo.count(), Constants.PAGE_SIZE);
		Pageable sortByRequestedField = PageRequest.of(Integer.parseInt(page)-1, Constants.PAGE_SIZE, Sort.by(SortWithField.CREATION_DATE.toString()));
		Page<VSImage> requestedImages = imageRepo.findAll(sortByRequestedField);
		Map<String, Object> attributesMap = new HashMap<String, Object>(){{put("images", requestedImages.getContent()); put("totalpages", totalPages); put("currentpage", page); }};
		model.addAllAttributes(attributesMap);
		return "staff/images/list";
	}
}
