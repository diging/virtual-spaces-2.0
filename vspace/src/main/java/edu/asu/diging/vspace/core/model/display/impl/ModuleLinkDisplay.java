package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;

@Entity
public class ModuleLinkDisplay extends LinkDisplay implements IModuleLinkDisplay {

    @Id
    @GeneratedValue(generator = "modulelink_display_id_generator")
    @GenericGenerator(name = "modulelink_display_id_generator", 
      parameters = @Parameter(name = "prefix", value = "MDLD"),
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = ModuleLink.class)
    private IModuleLink link;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IModuleLinkDisplay#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.IModuleLinkDisplay#setId(java.
     * lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public IModuleLink getLink() {
        return link;
    }

    @Override
    public void setLink(IModuleLink link) {
        this.link = link;
    }
}
