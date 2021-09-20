package edu.asu.diging.vspace.core.services;
import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;

public interface IPublicSearchManager {

    Page<ISpace> searchInSpaces(String search, int page);

    Page<IModule> searchInModules(String search, int page);

    Page<ISlide> searchInSlides(String search, int page);

    Page<ISlide> searchInSlideTexts(String search, int page);
    
}
