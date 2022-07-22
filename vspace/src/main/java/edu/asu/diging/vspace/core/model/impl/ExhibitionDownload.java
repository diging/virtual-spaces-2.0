package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExhibitionDownload;

@Entity
public class ExhibitionDownload extends VSpaceElement  implements IExhibitionDownload{

    @Id
    @GeneratedValue(generator = "exhibit_download_id_generator")
    @GenericGenerator(name = "exhibit_download_id_generator", parameters = @Parameter(name = "prefix", value = "EXHDWNLD"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    private String folderPath;
    

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

}
