package edu.asu.diging.vspace.core.model.impl;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISnapshotTask;

@Entity
public class SnapshotTask extends VSpaceElement implements ISnapshotTask {
    @Id
    @GeneratedValue(generator = "snapshot_task_id_generator")
    @GenericGenerator(name = "snapshot_task_id_generator", parameters = @Parameter(name = "prefix", value = "TASK"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private boolean isTaskComplete = false;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exhibitionSnapshotId", referencedColumnName = "id")
    private ExhibitionSnapshot exhibitionSnapshot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public boolean isTaskComplete() {
        return isTaskComplete;
    }
    
    @Override
    public void setTaskComplete(boolean isTaskComplete) {
        this.isTaskComplete = isTaskComplete;
    }
    
    @Override
    public ExhibitionSnapshot getExhibitionSnapshot() {
        return exhibitionSnapshot;
    }

    @Override
    public void setExhibitionSnapshot(ExhibitionSnapshot exhibitionSnapshot) {
        this.exhibitionSnapshot = exhibitionSnapshot;
    }
    
    
}
