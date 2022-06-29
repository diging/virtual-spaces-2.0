package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSpaceResults;

/**
 * This class has all the methods with the business logics for searching a word
 * in staff page.
 * 
 * @author Avirup Biswas
 *
 */
@Service
public class StaffSearchManager extends SearchManager implements IStaffSearchManager {

    @Autowired
    private ISpaceManager spaceManager;

    @Override
    protected Page<ISpace> spaceSearch(Pageable requestedPageForSpace, String searchTerm) {
        return spaceManager.findByNameOrDescription(requestedPageForSpace, searchTerm);
    }


}
