package edu.asu.diging.vspace.core.services.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Service
public class StaffSearchManager implements IStaffSearchManager {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private TextContentBlockRepository textContentBlockRepo;

    @Override
    public HashSet<IVSpaceElement> getAllSearchedSpaces(String search, Pageable requestedPage) {

        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(spaceManager.findInNameOrDescription(requestedPage, search).getContent());
        return resultElements;
    }

    /**
     * This method is used to search the given text in the name and description
     * field of space, module and slide tables.
     * 
     * @param search - This is the string entered in the search box.
     * @return resultElements - Has all relevant VSpaceElements with the search
     *         texts.
     */
    @Override
    public HashSet<IVSpaceElement> getAllSearchedModules(String search, Pageable requestedPage) {

        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(moduleManager.findInNameOrDescription(requestedPage, search).getContent());
        return resultElements;
    }

    @Override
    public HashSet<IVSpaceElement> getAllSearchedSlides(String search, Pageable requestedPage) {

        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(slideManager.findInNameOrDescription(requestedPage, search).getContent());
        return resultElements;
    }

    @Override
    public HashSet<IVSpaceElement> getAllSearchedSlideTexts(String search, Pageable requestedPage) {

        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(textContentBlockRepo.findInNameOrDescription(requestedPage, search).getContent());
        return resultElements;
    }

    @Override
    public int getCountOfSearchedModule(String search) {
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(moduleManager.findInNameOrDescription(search));
        return resultElements.size();
    }

    @Override
    public int getCountOfSearchedSpace(String search) {
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(spaceManager.findInNameOrDescription(search));
        return resultElements.size();
    }

    @Override
    public int getCountOfSearchedSlide(String search) {
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(slideManager.findInNameOrDescription(search));
        return resultElements.size();
    }

    @Override
    public int getCountOfSearchedSlideText(String search) {
        HashSet<IVSpaceElement> resultElements = new LinkedHashSet<>();
        resultElements.addAll(textContentBlockRepo.findInNameOrDescription(search));
        return resultElements.size();
    }

}
