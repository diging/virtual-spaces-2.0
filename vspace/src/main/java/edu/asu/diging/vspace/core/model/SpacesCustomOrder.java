package edu.asu.diging.vspace.core.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class SpacesCustomOrder extends VSpaceElement implements ISpacesCustomOrder {
    
    @Id
    @GeneratedValue(generator = "customorder_id_generator")
    @GenericGenerator(name = "customorder_id_generator",
        parameters = @Parameter(name = "prefix", value = "CON"),
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Space.class)
    private ISpace space;

    private Integer customOrder;
    
    public SpacesCustomOrder() {
        
    }
    
    public SpacesCustomOrder(ISpace space, Integer customOrder) {
        this.space = space;
        this.customOrder= customOrder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IContentBlock#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IContentBlock#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public Integer getCustomOrder() {
        return customOrder;
    }

    @Override
    public void setCustomOrder(Integer customOrder) {
        this.customOrder=customOrder;
    }
    
    @Override
    public ISpace getSpace() {
        return space;
    }

}
