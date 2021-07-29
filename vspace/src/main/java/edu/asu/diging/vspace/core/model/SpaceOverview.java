package edu.asu.diging.vspace.core.model;

public class SpaceOverview {
    
 private String name;
 
 private String id;
 
 private String link;
 
 private String img;
 
 private boolean module;
 
 private boolean unpublished;
 
 private boolean hideIncomingLinks;
 
 private String source;
 
 private String target;

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

public String getSource() {
    return source;
}

public void setSource(String source) {
    this.source = source;
}

public String getTarget() {
    return target;
}

public void setTarget(String target) {
    this.target = target;
}

}
