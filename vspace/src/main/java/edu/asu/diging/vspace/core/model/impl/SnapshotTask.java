package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IExhibitionDownload;
import edu.asu.diging.vspace.core.model.ISnapshotTask;

@Entity
public class SnapshotTask extends VSpaceElement implements ISnapshotTask {
    @Id
    @GeneratedValue(generator = "snapshot_task_id_generator")
    @GenericGenerator(name = "snapshot_task_id_generator", parameters = @Parameter(name = "prefix", value = "TASK"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private boolean isTaskComplete = false;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exhibitionDownloadId", referencedColumnName = "id")

    ExhibitionDownload exhibitionDownload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public boolean isTaskComplete() {
        return isTaskComplete;
    }

    public void setTaskComplete(boolean isTaskComplete) {
        this.isTaskComplete = isTaskComplete;
    }
    
    
    public ExhibitionDownload getExhibitionDownload() {
        return exhibitionDownload;
    }

    public void setExhibitionDownload(ExhibitionDownload exhibitionDownload) {
        this.exhibitionDownload = exhibitionDownload;
    }
    
    
}
