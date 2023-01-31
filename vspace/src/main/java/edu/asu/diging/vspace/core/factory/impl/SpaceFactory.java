package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LanguageDescriptionObjectRepository;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFactory implements ISpaceFactory {
    
    @Autowired
    ExhibitionLanguageRepository exhibitionLanguageRepository;
    
    @Autowired
    LanguageDescriptionObjectRepository languageDescriptionObjectRepo;

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.factory.impl.ISpaceFactory#createSpace(edu.asu.diging.vspace.web.staff.forms.SpaceForm)
	 */
	@Override
	public ISpace createSpace(SpaceForm form) {
		ISpace space = new Space();
		space.setName(form.getName());
		space.setDescription(form.getDescription());	
	
		form.getDescriptions().forEach(description -> {
		    
		    setSpaceDescription(space, description ) ;
		
		});
		form.getNames().forEach(name ->  { 
		    
		    setSpaceName(space, name);
		});
//		space.setSpaceTitles(form.getNames());
//		space.setSpaceDescriptions(form.getDescriptions());
		return space;
	}

    
}
