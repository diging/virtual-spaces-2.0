package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.SlideExternalLinkRepository;
import edu.asu.diging.vspace.core.factory.ISlideExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISlideExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
@Service
public class SlideExternalLinkManager extends SlideLinkManager<IExternalLinkSlide, ExternalLinkValue, ISlideExternalLinkDisplay>
implements ISlideExternalLinkManager{
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private SlideExternalLinkRepository externalLinkRepo;

    @Autowired
    private SlideExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Autowired
    private ISlideExternalLinkFactory externalLinkFactory;

    @Autowired
    private ISlideExternalLinkDisplayFactory externalLinkDisplayFactory;
    
    public List<IExternalLinkSlide> getLinks(String slideId) {
        return externalLinkRepo.findExternalLinkSlides(slideId);
    }
    
	@Override
	public List<ISlideExternalLinkDisplay> getLinkDisplays(String slideId) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    protected IExternalLinkSlide createLinkObject(String title, String id) {
        ISlide source = slideManager.getSlide(id);
        IExternalLinkSlide link = externalLinkFactory.createExternalLink(title, source);
        return externalLinkRepo.save((ExternalLinkSlide) link);
    }
    
    @Override
    protected ExternalLinkValue getTarget(String externalLink) {
        return new ExternalLinkValue(externalLink);
    }

    @Override
    protected ISlideExternalLinkDisplay updateLinkAndDisplay(IExternalLinkSlide link, ISlideExternalLinkDisplay displayLink) {
        externalLinkRepo.save((ExternalLinkSlide) link);
        return externalLinkDisplayRepo.save((SlideExternalLinkDisplay) displayLink);
    }

    @Override
    protected ISlideExternalLinkDisplay getDisplayLink(String externalLinkDisplayId) {
        Optional<SlideExternalLinkDisplay> externalLinkDisplay = externalLinkDisplayRepo.findById(externalLinkDisplayId);
        if (externalLinkDisplay.isPresent()) {
            return externalLinkDisplay.get();
        }
        return null;
    }

    @Override
    protected IExternalLinkSlide getLink(String externalLinkID) {
        Optional<ExternalLinkSlide> externalLink = externalLinkRepo.findById(externalLinkID);
        if (externalLink.isPresent()) {
            return externalLink.get();
        }
        return null;
    }

    @Override
    protected ISlideExternalLinkDisplay createDisplayLink(IExternalLinkSlide link) {
        return externalLinkDisplayFactory.createExternalLinkDisplay(link);
    }

    @Override
    protected void deleteLinkDisplayRepo(IExternalLinkSlide link) {
        externalLinkDisplayRepo.deleteByExternalLink(link);
    }

    @Override
    protected void removeFromLinkList(ISlide slide, IExternalLinkSlide link) {
        slide.getExternalLinks().remove(link);
    }

    @Override
    protected void deleteLinkRepo(IExternalLinkSlide link) {
        externalLinkRepo.delete((ExternalLinkSlide) link);
    }

    @Override
    public IExternalLinkSlide createExternalLink(String title, String url, String slideId) {
    	ISlide slide = slideManager.getSlide(slideId);
    	IExternalLinkSlide externalLink = new ExternalLinkSlide();
    	externalLink.setExternalLink(url);
    	externalLink.setName(title);
    	externalLink.setSlide(slide);
    	externalLinkRepo.save((ExternalLinkSlide) externalLink);
    	return externalLink;
    }
    
    @Override
    public IExternalLinkSlide updateExternalLink(String title, String url, String id) {
    	IExternalLinkSlide externalLink = getLink(id);
    	if (externalLink != null) {
			externalLink.setExternalLink(url);
	    	externalLink.setName(title);
	    	externalLinkRepo.save((ExternalLinkSlide) externalLink);
    	} else {
    		externalLink = new ExternalLinkSlide();
    	}
    	return externalLink;
    }
}
