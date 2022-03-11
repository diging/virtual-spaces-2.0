package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IPrefix;
import edu.asu.diging.vspace.core.model.IVSFile;

@Entity
public class VSFile implements IVSFile {
    @Id
    @GeneratedValue(generator = "file_id_generator")
    @GenericGenerator(name = "space_id_generator", parameters = @Parameter(name = "prefix", value = IPrefix.SPACE_PREFIX), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    String filename;
}
