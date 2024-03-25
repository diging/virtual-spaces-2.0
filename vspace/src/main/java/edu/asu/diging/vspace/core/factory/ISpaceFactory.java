package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

public interface ISpaceFactory {
    
    public ISpace createSpace(SpaceForm form);
    
    public void addSpaceDescription(ISpace space, LocalizedTextForm description);

}