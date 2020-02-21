package edu.asu.diging.vspace.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;

@Controller
public class ExhibitionModuleController {

	@Autowired
	private IModuleManager moduleManager;

	@Autowired
	private ContentBlockManager contentBlockManager;

	@RequestMapping(value = "/exhibit/module/{id}")
	public String module(@PathVariable("id") String id, Model model, RedirectAttributes attributes) {
		IModule module=moduleManager.getModule(id);
		model.addAttribute("module", module);
		try {
			List<List<IContentBlock>> slideContents=new ArrayList<List<IContentBlock>>();
			List<ISlide> slides = module.getStartSequence().getSlides();
			Iterator<ISlide> iter=slides.iterator();
			while(iter.hasNext()) {
				ISlide slide=iter.next();
				slideContents.add(contentBlockManager.getAllContentBlocks(slide.getId()));
			}
			model.addAttribute("slideContents", slideContents);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "module";
	}
}
