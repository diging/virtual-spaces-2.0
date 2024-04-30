package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

public interface ISpaceFormFactory {
    
    SpaceForm createNewSpaceForm(ISpace space, IExhibition startExhibtion);
    
    SpaceForm getSpaceForm(ISpace space, IExhibition startExhibtion);

}
