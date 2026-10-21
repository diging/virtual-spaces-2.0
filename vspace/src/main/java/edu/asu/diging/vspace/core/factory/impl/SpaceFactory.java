package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFactory implements ISpaceFactory {

    /**
     * Creates a new space based on the provided form.
     * @param form The form containing details for creating the space.
     * @return The newly created space and saves it to the repository.
     */
    @Override
    public ISpace createSpace(SpaceForm form) {
        ISpace space = new Space();
        space.setName(form.getName());
        space.setDescription(form.getDescription());
        return space;
    }	
}