package edu.asu.diging.vspace.core.services;

import java.util.HashSet;

import org.springframework.data.domain.Pageable;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

public interface IStaffSearchManager {

    HashSet<IVSpaceElement> getAllSearchedSpaces(String search, Pageable requestedPage);

    HashSet<IVSpaceElement> getAllSearchedModules(String search, Pageable requestedPage);

    HashSet<IVSpaceElement> getAllSearchedSlides(String search, Pageable requestedPage);

    HashSet<IVSpaceElement> getAllSearchedSlideTexts(String search, Pageable requestedPage);

    int getCountOfSearchedModule(String search);

    int getCountOfSearchedSpace(String search);

    int getCountOfSearchedSlide(String search);

    int getCountOfSearchedSlideText(String search);
}
