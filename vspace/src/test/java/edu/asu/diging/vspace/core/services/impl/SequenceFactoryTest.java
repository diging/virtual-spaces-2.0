package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.asu.diging.vspace.core.factory.impl.SequenceFactory;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public class SequenceFactoryTest {

    private SequenceFactory sequenceFactoryToTest = new SequenceFactory();
    
    @Test
    public void test_createSequence_success() {
        Module module = new Module();
        module.setId("module1");
        
        SequenceForm sequenceForm = new SequenceForm();
        sequenceForm.setName("SEQ1");
        sequenceForm.setDescription("sample description");
        
        Slide slide1 = new Slide();
        slide1.setId("slide1");       
        Slide slide2 = new Slide();
        slide2.setId("slide2");
 
        List<ISlide> slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);

        ISequence actualSequence = sequenceFactoryToTest.createSequence(module, sequenceForm, slides);
        Assert.assertEquals(sequenceForm.getName(), actualSequence.getName());
        Assert.assertEquals(sequenceForm.getDescription(), actualSequence.getDescription());
        Assert.assertEquals(module.getId(), actualSequence.getModule().getId());
        Assert.assertEquals(slides, actualSequence.getSlides());
    }
}
