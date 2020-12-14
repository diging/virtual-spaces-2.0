package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class VSMedia extends VSpaceElement{
    
    @Lob
    private String filename;
    @Lob
    private String parentPath;
    private String fileType;

    private int height;
    private int width;

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        
    }

}
