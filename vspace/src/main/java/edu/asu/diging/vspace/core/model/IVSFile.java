package edu.asu.diging.vspace.core.model;


public interface IVSFile extends IVSpaceElement {
    
    String getFilename();

    void setFilename(String filename);

    String getParentPath();

    void setParentPath(String parentPath);

    void setFileType(String fileType);

    String getFileType();

    String getOriginalFileName();

    void setOriginalFileName(String originalFileName);

    String getFileDescription();

    void setFileDescription(String fileDescription);

}
