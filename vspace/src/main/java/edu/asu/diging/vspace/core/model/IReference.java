package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.BiblioBlock;

public interface IReference extends IVSpaceElement {
    
    List<BiblioBlock> getBiblios();

    void setBiblios(List<BiblioBlock> biblios);
    
    void setTitle(String title);

    String getTitle();
    
    void setAuthor(String author);

    String getAuthor();

    String getYear();
    
    void setYear(String year);

    String getJournal();
    
    void setJournal(String journal);
    
    String getUrl();
    
    void setUrl(String url);
    
    String getVolume();
    
    void setVolume(String volume);
    
    String getIssue();
    
    void setIssue(String issue);
    
    String getPages();
    
    void setPages(String pages);
    
    String getEditors();
    
    void setEditors(String editors);
    
    String getType();
    
    void setType(String type);
    
    String getNote();
    
    void setNote(String note);
    
    void setId(String id);

    String getId();

}
