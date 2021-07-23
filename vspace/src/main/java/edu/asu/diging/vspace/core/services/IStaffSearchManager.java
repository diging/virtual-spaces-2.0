package edu.asu.diging.vspace.core.services;

import java.util.HashSet;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.Module;

public interface IStaffSearchManager {

    Page<Space> searchInSpaces(String searchTerm, int page);

    Page<Module> searchInModules(String searchTerm, int page);

    Page<Slide> searchInSlides(String searchTerm, int page);

    Page<Slide> searchInSlideTexts(String searchTerm, int page);

    long getTotalSpacePages(String searchTerm);

    long getTotalModulePages(String searchTerm);

    long getTotalSlidePages(String searchTerm);

    long getTotalSlideTextPages(String searchTerm);
}
