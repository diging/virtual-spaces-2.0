package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

@Entity
public class SpaceLinkDisplay extends LinkDisplay implements ISpaceLinkDisplay {

    @Id
    @GeneratedValue(generator = "link-display-id-generator")
    @GenericGenerator(name = "link-display-id-generator", parameters = @Parameter(name = "prefix", value = "SPLD000000000"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private int rotation;
    private DisplayType type;

    @OneToOne(targetEntity = SpaceLink.class)
    private ISpaceLink link;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISpaceLinkDisplay#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISpaceLinkDisplay#setId(java.
     * lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public ISpaceLink getLink() {
        return link;
    }

    @Override
    public void setLink(ISpaceLink link) {
        this.link = link;
    }

    @Override
    public int getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public DisplayType getType() {
        return type;
    }

    @Override
    public void setType(DisplayType type) {
        this.type = type;
    }
}
