package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Lob;

import edu.asu.diging.vspace.core.model.IVSMedia;

public class VSMedia extends VSpaceElement implements IVSMedia {
    
    private String id;
    @Lob
    private String filename;
    @Lob
    private String parentPath;
    private String fileType;

    private int height;
    private int width;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
