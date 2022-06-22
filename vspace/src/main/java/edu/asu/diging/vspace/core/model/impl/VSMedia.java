package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import edu.asu.diging.vspace.core.model.IVSMedia;

@MappedSuperclass
public abstract class VSMedia extends VSpaceElement implements IVSMedia{
    
    @Lob
    private String filename;
    
    private String fileType;  

    @Lob
    private String parentPath;
    
    @Override
    public String getFileType() {
        return fileType;
    }
    
    @Override
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IVSMedia#getFilename()
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IVSMedia#setFilename(java.lang.String)
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IVSMedia#getParentPath()
     */
    @Override
    public String getParentPath() {
        return parentPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IVSMedia#setParentPath(java.lang.String)
     */
    @Override
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}
