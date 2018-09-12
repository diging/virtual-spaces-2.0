package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IExhibitionFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;

@Service
public class ExhibitionFactory implements IExhibitionFactory {

	@Autowired
	private ExhibitionManager exhibitManager;
	@Override
	public IExhibition createSpace(String exhibitID) {
		if(exhibitID.equals("New"))
			return new Exhibition();
		else
			return exhibitManager.getExhibitionById(exhibitID);
	}
}
