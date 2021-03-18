package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IVSVideo;

@Entity
public class VSVideo extends VSMedia implements IVSVideo{

    @Id
    @GeneratedValue(generator = "video_id_generator")
    @GenericGenerator(name = "video_id_generator", parameters = @Parameter(name = "prefix", value = "VID"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private String url;
    
    private Long fileSize; 
    
    private String title;

    @Override
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public Long getFileSize() {
        return fileSize;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
        
    }
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

}
