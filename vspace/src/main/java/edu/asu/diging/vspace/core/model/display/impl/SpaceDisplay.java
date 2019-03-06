package edu.asu.diging.vspace.core.model.display.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;

@Entity
public class SpaceDisplay implements ISpaceDisplay {

    @Id
    @GeneratedValue(generator = "space_display_id_generator")
    @GenericGenerator(name = "space_display_id_generator", parameters = @Parameter(name = "prefix", value = "SPD"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Space.class)
    private ISpace space;

    private int width;
    private int height;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#setId(java.lang.
     * String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#getSpace()
     */
    @Override
    public ISpace getSpace() {
        return space;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#setSpace(edu.asu.
     * diging.vspace.core.model.ISpace)
     */
    @Override
    public void setSpace(ISpace space) {
        this.space = space;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#getWidth()
     */
    @Override
    public int getWidth() {
        return width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#setWidth(int)
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#getHeight()
     */
    @Override
    public int getHeight() {
        return height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.display.impl.ISpaceDisplay#setHeight(int)
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

}
