package edu.asu.diging.vspace.core.model;

public interface IVSMedia extends IVSpaceElement{

    String getFilename();

    void setFilename(String filename);

    String getParentPath();

    void setParentPath(String parentPath);

    void setFileType(String fileType);

    String getFileType();

    void setWidth(int width);

    int getWidth();

    void setHeight(int height);

    int getHeight();
    
}