package edu.asu.diging.vspace.core.model;


public interface IVSFile extends IVSMedia {
    
    String getFilename();

    void setFilename(String filename);

    void setFileType(String fileType);

    String getFileType();

    String getOriginalFileName();

    void setOriginalFileName(String originalFileName);

    String getFileDescription();

    void setFileDescription(String fileDescription);

}
