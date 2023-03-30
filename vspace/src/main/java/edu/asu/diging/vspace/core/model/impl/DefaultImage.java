package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import edu.asu.diging.vspace.core.model.IDefaultImage;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class DefaultImage implements IDefaultImage{
    
    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private IVSImage spacelinkDefaultImage;
    
    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private IVSImage modulelinkDefaultImage;
    
    @OneToOne(targetEntity = VSImage.class)
    @NotFound(action = NotFoundAction.IGNORE)
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
