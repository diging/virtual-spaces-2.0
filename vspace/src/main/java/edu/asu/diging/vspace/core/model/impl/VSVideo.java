package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IVSVideo;

@Entity
public class VSVideo extends VSMedia implements IVSVideo{

    @Id
    @GeneratedValue(generator = "video_id_generator")
    @GenericGenerator(name = "video_id_generator", parameters = @Parameter(name = "prefix", value = "VID"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private Long fileSize; 

    @Override
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public Long getFileSize() {
        return fileSize;
    }

}
