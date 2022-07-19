package edu.asu.diging.vspace.core.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;

public interface IPublicSearchManager extends ISearchManager{

    public List<ISlide> updateSlidePageWithSpaceInfo(Page<ISlide> slidePage);
    
    public List<IModule> updateModuleListWithSpaceInfo(Page<IModule> modulePage);

    public List<ISlide> updateSlideTextPageWithSpaceInfo(Page<ISlide> slideTextPage);

}
