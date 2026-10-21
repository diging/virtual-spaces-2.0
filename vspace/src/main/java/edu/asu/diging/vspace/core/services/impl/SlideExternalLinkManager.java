package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SlideExternalLinkRepository;
import edu.asu.diging.vspace.core.model.ISlideExternalLink;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.SlideExternalLink;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
@Service
/**
 * This class contains CRUD methods for managing the external links
 * (ISlideExternalLink) associated with module slides only
 * 
 * @author Julian Ophals
 */
public class SlideExternalLinkManager
        implements ISlideExternalLinkManager {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private SlideExternalLinkRepository externalLinkRepo;

    @Override
    public ISlideExternalLink getLink(String externalLinkID) {
        Optional<SlideExternalLink> externalLink = externalLinkRepo.findById(externalLinkID);
        if (externalLink.isPresent()) {
            return externalLink.get();
        }
        return null;
    }

    @Override
    public void deleteLink(String linkId) {
        ISlideExternalLink link = getLink(linkId);
        externalLinkRepo.delete((SlideExternalLink) link);
    }

    /**
     * This method is used to create a link to an external web address that will
     * be displayed on the associated slide; this method does NOT save the newly 
     * created external link
     * @param   label   the hyperlink text
     * @param   url     the full web address of the external link        
     * @param   slideId the id of the slide in which the link will be created
     * @return  IExternalLinkSlide  returns the external link that was created 
     */ 
    @Override
    public ISlideExternalLink createExternalLink(String label, String url, String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        ISlideExternalLink externalLink = new SlideExternalLink();
        externalLink.setExternalLink(url);
        externalLink.setLabel(label);
        externalLink.setSlide(slide);
        return externalLink;
    }

    @Override
    public ISlideExternalLink updateExternalLink(String label, String url, String id) {
        ISlideExternalLink externalLink = getLink(id);
        if (externalLink != null) {
            externalLink.setExternalLink(url);
            externalLink.setLabel(label);
            externalLinkRepo.save((SlideExternalLink) externalLink);
        }
        return externalLink;
    }
}
