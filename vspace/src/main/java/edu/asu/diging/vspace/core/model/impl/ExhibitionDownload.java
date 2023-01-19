package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IExhibitionDownload;
import edu.asu.diging.vspace.core.model.ISnapshotTask;

@Entity
public class ExhibitionDownload extends VSpaceElement implements IExhibitionDownload{

    @Id
    @GeneratedValue(generator = "exhibit_download_id_generator")
    @GenericGenerator(name = "exhibit_download_id_generator", parameters = @Parameter(name = "prefix", value = "EXHDWNLD"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private String folderPath;

    private String folderName;
   
    @OneToOne(cascade = CascadeType.ALL, mappedBy="exhibitionDownload")
    @JsonIgnore
    private SnapshotTask snapshotTask;
    
    
    public ExhibitionDownload() {
        super();
    }    

    public ExhibitionDownload(String folderPath, String folderName) {
        super();
        this.folderPath = folderPath;
        this.folderName = folderName;
    }
    
    @Override
    public String getId() {
        return this.id;    }

    @Override
    public void setId(String id) {
        this.id = id;

    }
    
    @Override
    public String getFolderPath() {
        return folderPath;
    }

    @Override
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public String getFolderName() {
        return folderName;
    }

    @Override
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public SnapshotTask getSnapshotTask() {
        return snapshotTask;
    }

    @Override
    public void setSnapshotTask(SnapshotTask snapshotTask) {
        this.snapshotTask = snapshotTask;
    }

}
