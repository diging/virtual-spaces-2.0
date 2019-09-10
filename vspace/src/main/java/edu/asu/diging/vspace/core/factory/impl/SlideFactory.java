package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
    public IBranchingPoint createBranchingPoint(IModule moduleId, SlideForm form, List<IChoice> choices) {
        IBranchingPoint branchingPoint = new BranchingPoint();
        //IChoice choices = new Choice();
        branchingPoint.setName(form.getName());
        branchingPoint.setDescription(form.getDescription());
        branchingPoint.setModule(moduleId);
        branchingPoint.setContents(new ArrayList<IContentBlock>());
        branchingPoint.setChoices(choices);
        //branchingPoint.setChoices(form.getChoices());
        return branchingPoint;
    }    
    
}