package edu.asu.diging.vspace.core.model;

import java.util.List;

public class ModuleOverview{
 
    private String name;

    private String id;

    private String link;
    
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
    
    public List<String> getEdges() {
        return edges;
    }

    public void setEdges(List<String> edges) {
        this.edges = edges;
    }
    
    
}
