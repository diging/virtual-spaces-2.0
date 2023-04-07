package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IDefaultImage;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class DefaultImage implements IDefaultImage{
    
    @Id
    @GeneratedValue(generator = "image_id_generator")
    @GenericGenerator(name = "image_id_generator", parameters = @Parameter(name = "prefix", value = "IMG"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    
    private IVSImage spacelinkDefaultImage;
    
    
    private IVSImage modulelinkDefaultImage;
    
    
    private IVSImage externallinkDefaultImage;
    
    @Override
    public IVSImage getSpacelinkDefaultImage() {
        return spacelinkDefaultImage;
    }
    @Override
    public void setSpacelinkDefaultImage(IVSImage spacelinkDefaultImage) {
        this.spacelinkDefaultImage = spacelinkDefaultImage;
    }
    @Override
    public IVSImage getModulelinkDefaultImage() {
        return modulelinkDefaultImage;
    }
    @Override
    public void setModulelinkDefaultImage(IVSImage modulelinkDefaultImage) {
        this.modulelinkDefaultImage = modulelinkDefaultImage;
    }
    @Override
    public IVSImage getExternallinkDefaultImage() {
        return externallinkDefaultImage;
    }
    @Override
    public void setExternallinkDefaultImage(IVSImage externallinkDefaultImage) {
        this.externallinkDefaultImage = externallinkDefaultImage;
    }
    
    

}
