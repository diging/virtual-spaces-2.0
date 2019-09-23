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
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createSlide(java.lang.
     * String, java.lang.String)
     */
    @Override
    public ISlide createSlide(IModule module, SlideForm form) {
            ISlide slide = new Slide();        
            slide.setContents(new ArrayList<IContentBlock>());
            slide.setName(form.getName());
            slide.setDescription(form.getDescription());
            slide.setModule(module);
            return slide;        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createBranchingPoint(java.lang.
     * String, java.lang.String)
     */
    @Override
    public IBranchingPoint createBranchingPoint(IModule moduleId, SlideForm form) {
        IBranchingPoint branchingPoint = new BranchingPoint();
        branchingPoint.setName(form.getName());
        branchingPoint.setDescription(form.getDescription());
        branchingPoint.setModule(moduleId);
        branchingPoint.setContents(new ArrayList<IContentBlock>());
       
        List<IChoice> choices = choiceFactory.createChoices(form.getChoices(), branchingPoint);
        branchingPoint.setChoices(choices);
        return branchingPoint;
    }    
    
}