package edu.asu.diging.vspace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;

@Controller
public class ExhibitionSlideController {

	@Autowired
	private IModuleManager moduleManager;

	@Autowired
	private SlideManager sildeManager;

	@Autowired
	private ISequenceManager sequenceManager;

	@RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}", method = RequestMethod.GET)
	public String slide(Model model, @PathVariable("slideId") String slideId, @PathVariable("moduleId") String moduleId,
	        @PathVariable("sequenceId") String sequenceId) {
		IModule module = moduleManager.getModule(moduleId);
		model.addAttribute("module", module);
		if (module.getStartSequence() == null) {
			model.addAttribute("error", "Sorry, this module has not been configured yet.");
		} else {
			model.addAttribute("startSequenceId", module.getStartSequence().getId());
			List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
			if (slides.size() == 0) {
				model.addAttribute("error", "No slides to display in selected sequence for module.");
			} else {
				model.addAttribute("firstSlide", module.getStartSequence().getSlides().get(0).getId());
				model.addAttribute("slides", sequenceManager.getSequence(sequenceId).getSlides());
				model.addAttribute("currentSequenceId", sequenceId);
				model.addAttribute("currentSlideCon", sildeManager.getSlide(slideId));
			}
		}
		return "module";
	}

	@RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}/slideShow", method = RequestMethod.GET)
	public String slideShow(Model model, @PathVariable("slideId") String slideId,
	        @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
		IModule module = moduleManager.getModule(moduleId);
		model.addAttribute("module", module);
		if (module.getStartSequence() == null) {
			model.addAttribute("error", "Sorry, this module has not been configured yet.");
			return "module";
		} else {
			String startSequenceId = module.getStartSequence().getId();
			model.addAttribute("startSequenceId", startSequenceId);
			List<ISlide> sequenceSlides = sequenceManager.getSequence(sequenceId).getSlides();
			if (sequenceSlides.size() == 0) {
				model.addAttribute("error", "No slides to display in selected sequence for module.");
			} else {
				model.addAttribute("firstSlide", module.getStartSequence().getSlides().get(0).getId());
				String nextSlideId = "";
				String prevSlideId = "";
				int slideIndex = 0;
				slideIndex = sequenceSlides.indexOf(sildeManager.getSlide(slideId));
				if (sequenceSlides.size() > slideIndex + 1) {
					nextSlideId = sequenceSlides.get(slideIndex + 1).getId();
				}
				if (slideIndex > 0) {
					prevSlideId = sequenceSlides.get(slideIndex - 1).getId();
				}
				model.addAttribute("slides", sequenceSlides);
				model.addAttribute("currentSequenceId", sequenceId);
				model.addAttribute("nextSlide", nextSlideId);
				model.addAttribute("prevSlide", prevSlideId);
				model.addAttribute("currentSlideCon", sildeManager.getSlide(slideId));
				model.addAttribute("numOfSlides", sequenceSlides.size());
				model.addAttribute("currentNumOfSlide", slideIndex + 1);
			}
			return "slideShow";
		}

	}
}
