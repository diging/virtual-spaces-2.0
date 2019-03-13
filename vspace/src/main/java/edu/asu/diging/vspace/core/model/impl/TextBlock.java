package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ITextBlock;

public class TextBlock extends ContentBlock implements ITextBlock{
    
    @Id
    @GeneratedValue(generator = "txtblock_id_generator")
    @GenericGenerator(name = "txtblock_id_generator", parameters = @Parameter(name = "prefix", value = "TBLK"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

}
