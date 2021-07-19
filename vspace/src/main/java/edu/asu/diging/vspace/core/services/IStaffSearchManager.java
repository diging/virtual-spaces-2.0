package edu.asu.diging.vspace.core.services;

import java.util.HashSet;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.Module;

public interface IStaffSearchManager {

    Page<Space> searchInSpaces(String search, int page);

    Page<Module> searchInModules(String search, int page);

    Page<Slide> searchInSlides(String search, int page);

    Page<Slide> searchInSlideTexts(String search, int page);

    long getTotalSpacePages(String search);

    long getTotalModulePages(String search);

    long getTotalSlidePages(String search);

    long getTotalSlideTextPages(String search);
}
