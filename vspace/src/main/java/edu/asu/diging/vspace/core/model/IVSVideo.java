package edu.asu.diging.vspace.core.model;

public interface IVSVideo extends IVSMedia {
    
    void setFileSize(Long fileSize);
    
    Long getFileSize();
    
    public String getUrl();

    public void setUrl(String url);
    
    public void setTitle(String title);
    
    public String getTitle();

}
