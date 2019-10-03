package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IChoiceFactory;
import edu.asu.diging.vspace.core.factory.ISlideFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideFactory implements ISlideFactory {
    
    @Autowired
    private IChoiceFactory choiceFactory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createSlide(edu.asu.diging.vspace.core.model.IModule, edu.asu.diging.vspace.web.staff.forms.SlideForm)
     */
    @Override
    public ISlide createSlide(IModule module, SlideForm form) {
        ISlide slide = new Slide();
        slide.setName(form.getName());
        slide.setDescription(form.getDescription());
        slide.setModule(module);
        slide.setContents(new ArrayList<IContentBlock>());
        return slide;        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createBranchingPoint(edu.asu.diging.vspace.core.model.IModule, edu.asu.diging.vspace.web.staff.forms.SlideForm)
     */
    @Override
    public IBranchingPoint createBranchingPoint(IModule module, SlideForm form) {
        IBranchingPoint branchingPoint = new BranchingPoint();
        branchingPoint.setName(form.getName());
        branchingPoint.setDescription(form.getDescription());
        branchingPoint.setModule(module);
        branchingPoint.setContents(new ArrayList<IContentBlock>());
       
        List<IChoice> choices = choiceFactory.createChoices(form.getChoices());
        branchingPoint.setChoices(choices);
        return branchingPoint;
    }    
    
}