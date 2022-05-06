package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

public interface ISpaceFactory {

    ISpace createSpace(SpaceForm form);

}