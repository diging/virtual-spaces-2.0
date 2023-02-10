package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISlide;

public class SequenceOverview {

    private String name;

    private String id;

    private List<SlideOverview> slideOverviews;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SlideOverview> getSlideOverviews() {
        return slideOverviews;
    }

    public void setSlideOverviews(List<SlideOverview> slideOverviews) {
        this.slideOverviews = slideOverviews;
    }

   



}
