package edu.asu.diging.vspace.core.model;

public interface IVSVideo extends IVSpaceElement {

    String getFilename();

    void setFilename(String filename);
    
    void setFileSize(Long fileSize);
    
    Long getFileSize();

    void setFileType(String fileType);

    String getFileType();

    String getParentPath();

    void setParentPath(String parentPath);
    
    void setWidth(int width);

    int getWidth();

    void setHeight(int height);

    int getHeight();

    

}
