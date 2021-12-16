package edu.asu.diging.vspace.core.model;

import java.util.List;

public class ModuleOverview{
 
    private String name;

    private String id;
    
    private List<String> edges; //List of sequence ids

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
    
    public List<String> getEdges() {
        return edges;
    }

    public void setEdges(List<String> edges) {
        this.edges = edges;
    }
    
    
}
