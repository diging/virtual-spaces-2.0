package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.SpacesCustomOrder;
import edu.asu.diging.vspace.web.staff.ExhibitionSpaceOrderMode;

/**
 * Represents an exhibition that can have a default start space.
 * 
 * @see edu.asu.diging.vspace.core.model.IExhibition
 * @author Namratha
 */
@Entity
public class Exhibition extends VSpaceElement implements IExhibition {

    @Id
    @GeneratedValue(generator = "exhibit_id_generator")
    @GenericGenerator(name = "exhibit_id_generator", parameters = @Parameter(name = "prefix", value = "EXH"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Space.class)
    private ISpace startSpace;

    private String title;

    @Enumerated(EnumType.STRING)
    private ExhibitionModes mode;

    private String customMessage;
    
    @Enumerated(EnumType.STRING)
    private ExhibitionSpaceOrderMode spaceOrderMode;
    
    @OneToOne
    @JoinColumn(name = "Space_Custom_Order_Id", referencedColumnName = "Id")
    private SpacesCustomOrder spacesCustomOrder;

    @OneToOne(targetEntity = SpacesCustomOrder.class)
    private String customOrderName;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.IVSpaceElement#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.IVSpaceElement#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.IExhibition#getSpace()
     */
    @Override
    public ISpace getStartSpace() {
        return this.startSpace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.IExhibition#setSpace(edu.asu.diging.vspace.
     * core.model.ISpace)
     */
    @Override
    public void setStartSpace(ISpace space) {
        this.startSpace = space;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ExhibitionModes getMode() {
        return mode;
    }

    public void setMode(ExhibitionModes mode) {
        this.mode = mode;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
    
    @Override
    public ExhibitionSpaceOrderMode getSpaceOrderMode() {
        return spaceOrderMode;
    }

    @Override
    public void setSpaceOrderMode(ExhibitionSpaceOrderMode spaceOrderMode) {
        this.spaceOrderMode = spaceOrderMode;
    }
    
    @Override
    public SpacesCustomOrder getSpacesCustomOrder() {
        return spacesCustomOrder;
    }

    @Override
    public void setSpacesCustomOrder(SpacesCustomOrder spacesCustomOrder) {
        this.spacesCustomOrder = spacesCustomOrder;
    }

}
