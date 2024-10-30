package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;

public interface ISlideExternalLinkManager {

    IExternalLinkSlide getLink(String externalLinkID);
    
    IExternalLinkSlide createExternalLink(String title, String url, String slideId);

    IExternalLinkSlide updateExternalLink(String title, String url, String id);
    
    void deleteLink(String linkId);
}
