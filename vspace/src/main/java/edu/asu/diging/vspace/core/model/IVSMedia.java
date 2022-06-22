package edu.asu.diging.vspace.core.model;

public interface IVSMedia extends IVSpaceElement {

    public String getFilename() ;
    
    public void setFilename(String filename);
    
    public String getFileType();
    
    public void setFileType(String fileType);
    
    public String getParentPath();
    
    public void setParentPath(String parentPath);
}
