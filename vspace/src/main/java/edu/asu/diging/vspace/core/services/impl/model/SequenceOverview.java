package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISlide;

/**
 * Creates a network to overview the current sequence within the exhibition.
 * @author prachikharge
 *
 */
public class SequenceOverview {

    private String id;
    
    private String name;

    private List<SlideOverview> slideOverviews;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
  
    public List<SlideOverview> getSlideOverviews() {
        return slideOverviews;
    }

    public void setSlideOverviews(List<SlideOverview> slideOverviews) {
        this.slideOverviews = slideOverviews;
    }

}
