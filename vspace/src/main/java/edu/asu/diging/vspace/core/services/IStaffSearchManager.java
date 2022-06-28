package edu.asu.diging.vspace.core.services;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;

public interface IStaffSearchManager extends ISearchManager {

    Page<IModule> searchInModules(String searchTerm, int page);

    Page<ISlide> searchInSlides(String searchTerm, int page);

    Page<ISlide> searchInSlideTexts(String searchTerm, int page);
}
