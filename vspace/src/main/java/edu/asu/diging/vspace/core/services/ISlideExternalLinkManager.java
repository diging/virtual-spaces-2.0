package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISlideExternalLink;

public interface ISlideExternalLinkManager {

    ISlideExternalLink getLink(String externalLinkID);
    
    ISlideExternalLink createExternalLink(String title, String url, String slideId);

    ISlideExternalLink updateExternalLink(String title, String url, String id);
    
    void deleteLink(String linkId);
}
