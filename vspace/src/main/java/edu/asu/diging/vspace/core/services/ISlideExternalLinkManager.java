package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;

public interface ISlideExternalLinkManager extends ISlideLinkManager<IExternalLinkSlide, ExternalLinkValue, ISlideExternalLinkDisplay>{
    
	List<IExternalLinkSlide> getLinks(String slideId);
	
	IExternalLinkSlide createExternalLink(String title, String url, String slideId);
	
	IExternalLinkSlide updateExternalLink(String title, String url, String id);
}
