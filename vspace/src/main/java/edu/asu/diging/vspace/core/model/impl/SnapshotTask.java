package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
