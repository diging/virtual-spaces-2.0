package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideExternalLinkRepository;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
@Service
public class SlideExternalLinkManager
        extends SlideLinkManager<IExternalLinkSlide, ExternalLinkValue, ISlideExternalLinkDisplay>
        implements ISlideExternalLinkManager {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private SlideExternalLinkRepository externalLinkRepo;

    @Transactional
    public List<IExternalLinkSlide> getLinks(String slideId) {
        return externalLinkRepo.findBySlide(slideId);
    }

    @Override
    protected ExternalLinkValue getTarget(String externalLink) {
        return new ExternalLinkValue(externalLink);
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

    @Override
    protected void deleteLinkDisplayRepo(IExternalLinkSlide link) {        
    }

    @Override
    protected ISlideExternalLinkDisplay updateLinkAndDisplay(IExternalLinkSlide link,
            ISlideExternalLinkDisplay displayLink) {
        return null;
    }

    @Override
    protected ISlideExternalLinkDisplay getDisplayLink(String linkDisplayId) throws LinkDoesNotExistsException {
        return null;
    }

    @Override
    protected ISlideExternalLinkDisplay createDisplayLink(IExternalLinkSlide link) {
        return null;
    }

    @Override
    public List<ISlideExternalLinkDisplay> getLinkDisplays(String slideId) {
        return null;
    }


    @Override
    protected IExternalLinkSlide createLinkObject(String title, String id) {
        return null;
    }
}
