package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;

public class ExternalLinkDisplay extends VSpaceElement implements IExternalLinkDisplay {

    @Id
    @GeneratedValue(generator = "link-display-id-generator")
    @GenericGenerator(name = "externallink-display-id-generator",
      parameters = @Parameter(name = "prefix", value = "EXLD"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private float positionX;
    private float positionY;

    @OneToOne(targetEntity = ExternalLink.class)
    private IExternalLink externalLink;

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

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * getPositionX( )
     */
    @Override
    public float getPositionX() {
        return positionX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * setPositionX(float)
     */
    @Override
    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * getPositionY( )
     */
    @Override
    public float getPositionY() {
        return positionY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * setPositionY(float)
     */
    @Override
    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * getExternalLink(float)
     */
    @Override
    public IExternalLink getExternalLink() {
        return externalLink;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.IExternalLinkDisplay#
     * setExternalLink(IExternalLink)
     */
    @Override
    public void setExternalLink(IExternalLink externalLink) {
        this.externalLink = externalLink;
    }

}
