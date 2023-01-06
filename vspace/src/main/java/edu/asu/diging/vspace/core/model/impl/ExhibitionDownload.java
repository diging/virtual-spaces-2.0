package edu.asu.diging.vspace.core.model.impl;

import java.util.concurrent.Future;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExhibitionDownload;
import edu.asu.diging.vspace.core.model.ISnapshotTask;

@Entity
public class ExhibitionDownload extends VSpaceElement  implements IExhibitionDownload{

    @Id
    @GeneratedValue(generator = "exhibit_download_id_generator")
    @GenericGenerator(name = "exhibit_download_id_generator", parameters = @Parameter(name = "prefix", value = "EXHDWNLD"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private String folderPath;

    private String folderName;
    
//    private boolean isDownloadComplete;

    @OneToOne(targetEntity = SnapshotTask.class)
    private ISnapshotTask snapshotTask;
    
    public ISnapshotTask getSnapshotTask() {
        return snapshotTask;
    }

    public void setSnapshotTask(ISnapshotTask snapshotTask) {
        this.snapshotTask = snapshotTask;
    }

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

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }


    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    
//    public boolean isDownloadComplete() {
//        return isDownloadComplete;
//    }
//
//    public void setDownloadComplete(boolean isDownloadComplete) {
//        this.isDownloadComplete = isDownloadComplete;
//    }


}
