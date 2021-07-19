package edu.asu.diging.vspace.core.services;
import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface IPublicSearchManager {

    Page<Space> searchInSpaces(String search, int page);

    Page<Module> searchInModules(String search, int page);

    Page<Slide> searchInSlides(String search, int page);

    Page<Slide> searchInSlideTexts(String search, int page);

    long getTotalSpacePages(String search);

    long getTotalModulePages(String search);

    long getTotalSlidePages(String search);

    long getTotalSlideTextPages(String search);
}
