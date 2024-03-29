package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

public class SpaceOverview {

    private String name;

    private String id;

    private String link;

    private String img;

    private boolean module;

    private boolean unpublished;

    private boolean hideIncomingLinks;
    
    private List<String> edges;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isModule() {
        return module;
    }

    public void setModule(boolean module) {
        this.module = module;
    }

    public boolean isUnpublished() {
        return unpublished;
    }

    public void setUnpublished(boolean unpublished) {
        this.unpublished = unpublished;
    }

    public boolean isHideIncomingLinks() {
        return hideIncomingLinks;
    }

    public void setHideIncomingLinks(boolean hideIncomingLinks) {
        this.hideIncomingLinks = hideIncomingLinks;
    }

    public List<String> getEdges() {
        return edges;
    }

    public void setEdges(List<String> edges) {
        this.edges = edges;
    }
}
