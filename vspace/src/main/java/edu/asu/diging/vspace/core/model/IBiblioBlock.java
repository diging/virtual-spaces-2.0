package edu.asu.diging.vspace.core.model;

public interface IBiblioBlock extends IContentBlock {

    void setTitle(String title);

    String getTitle();
    
    void setAuthor(String author);

    String getAuthor();

    int getYear();
    
    void setYear(int year);

    String getJournal();
    
    void setJournal(String journal);

    void setId(String id);

    String getId();

}
