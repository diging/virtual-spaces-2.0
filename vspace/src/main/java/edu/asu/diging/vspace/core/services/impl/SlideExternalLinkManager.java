package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideExternalLinkRepository;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
@Service
public class SlideExternalLinkManager
        implements ISlideExternalLinkManager {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private SlideExternalLinkRepository externalLinkRepo;

    @Override
    public IExternalLinkSlide getLink(String externalLinkID) {
        Optional<ExternalLinkSlide> externalLink = externalLinkRepo.findById(externalLinkID);
        if (externalLink.isPresent()) {
            return externalLink.get();
        }
        return null;
    }

    @Override
    public void deleteLink(String linkId) {
        IExternalLinkSlide link = getLink(linkId);
        externalLinkRepo.delete((ExternalLinkSlide) link);
    }

    @Override
    public IExternalLinkSlide createExternalLink(String label, String url, String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        IExternalLinkSlide externalLink = new ExternalLinkSlide();
        externalLink.setExternalLink(url);
        externalLink.setLabel(label);
        externalLink.setSlide(slide);
        return externalLink;
    }

    @Override
    public IExternalLinkSlide updateExternalLink(String label, String url, String id) {
        IExternalLinkSlide externalLink = getLink(id);
        if (externalLink != null) {
            externalLink.setExternalLink(url);
            externalLink.setLabel(label);
            externalLinkRepo.save((ExternalLinkSlide) externalLink);
        }
        return externalLink;
    }
}
