package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IImageBlock;

public class ImageBlock extends ContentBlock implements IImageBlock {
    
    @Id
    @GeneratedValue(generator = "imgblock_id_generator")
    @GenericGenerator(name = "imgblock_id_generator", parameters = @Parameter(name = "prefix", value = "IBLK"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

}
