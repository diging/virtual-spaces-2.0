package edu.asu.diging.vspace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Controller
public class ExhibitionSequencesController {

	@Autowired
	private IModuleManager moduleManager;

	@Autowired
	private ISequenceManager sequenceManager;

	@RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{sequenceId}")
	public String sequence(Model model, @PathVariable("sequenceId") String sequenceId,
	        @PathVariable("moduleId") String moduleId) {
		IModule module = moduleManager.getModule(moduleId);
		model.addAttribute("module", module);
		List<ISequence> sequences = moduleManager.getModuleSequences(moduleId);
		List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
		boolean sequenceExist = sequences.stream().anyMatch(sequence -> sequence.getId().equals(sequenceId));
		if (!sequenceExist) {
			model.addAttribute("error", "Sequence does not belong to selected module.");
			return "module";
		} else if (slides.size() == 0) {
			model.addAttribute("error", "No slides to display in selected sequence for module.");
			return "module";
		}
		String firstSlideId = slides.get(0).getId();
		return "redirect:/exhibit/module/" + moduleId + "/sequence/" + sequenceId + "/slide/" + firstSlideId;
	}
}
