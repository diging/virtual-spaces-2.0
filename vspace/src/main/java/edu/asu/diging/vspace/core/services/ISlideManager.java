package edu.asu.diging.vspace.core.services;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideManager {

    ISlide getSlide(String slideId);

    ISlide createSlide(IModule module, SlideForm slideForm, SlideType type);

    void updateSlide(Slide slide);

    IBranchingPoint createBranchingPoint(IModule module, SlideForm slideForm, SlideType type);

    void updateBranchingPoint(IBranchingPoint branchingPoint, List<String> editedChoices);

    IChoice getChoice(String choiceId);

    void deleteSlideById(String slideId, String moduleId);

    List<Sequence> getSlideSequences(String slideId, String moduleId);
    
    Page<ISlide> findByNameOrDescription(Pageable requestedPage,String searchText);
    
    void updateNameAndDescription(ISlide slide, SlideForm slideForm);

    void addSlideLocalizedText(ISlide slide, LocalizedTextForm localizedTextFormData, List<ILocalizedText> localizedTextList);

    SlideForm getSlideForm(String slideId);

}
