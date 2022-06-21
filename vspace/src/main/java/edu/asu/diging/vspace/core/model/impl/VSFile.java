package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IVSFile;

@Entity

public class VSFile extends VSpaceElement implements IVSFile {

    @Id
    @GeneratedValue(generator = "file_id_generator")
    @GenericGenerator(name = "file_id_generator", parameters = @Parameter(name = "prefix", value = "FILE"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @Lob
    private String filename;
    @Lob
    private String parentPath;
    private String fileType;   
    private String originalFileName;
    private String fileDescription;

    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getFilename() {
        return filename;
    }
    
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    @Override
    public String getParentPath() {
        return parentPath;
    }
    
    @Override
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    @Override
    public String getOriginalFileName() {
        return originalFileName;
    }

    @Override
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    @Override
    public String getFileDescription() {
        return fileDescription;
    }

    @Override
    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }
    
    
}
