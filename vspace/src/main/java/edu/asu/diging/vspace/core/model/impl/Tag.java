package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ITag;

@Entity
public class Tag extends VSpaceElement implements ITag {

    @Id 
    @GeneratedValue(generator = "tag_id_generator")
    @GenericGenerator(name = "tag_id_generator", 
      parameters = @Parameter(name = "prefix", value = "TAG"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ITag#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.model.impl.ITag#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
}
