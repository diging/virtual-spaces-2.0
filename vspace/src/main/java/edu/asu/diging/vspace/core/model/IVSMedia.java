package edu.asu.diging.vspace.core.model;

/**
 * @author skhar
 *
 */
//IVSMedia is a class to hold common settings for Media classes
// IVSVideo and IVSImage classes.
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
