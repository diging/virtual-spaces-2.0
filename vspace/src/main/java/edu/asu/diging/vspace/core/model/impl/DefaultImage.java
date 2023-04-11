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
    
    
    
    

}
