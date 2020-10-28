package edu.asu.diging.vspace.core.model;

public interface IVSVideo extends IVSpaceElement {

    String getFilename();

    void setFilename(String filename);

    void setFileType(String fileType);

    String getFileType();

    void setWidth(int width);

    int getWidth();

    void setHeight(int height);

    int getHeight();

    

}
