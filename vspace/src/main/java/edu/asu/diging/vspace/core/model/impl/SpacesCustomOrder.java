package edu.asu.diging.vspace.core.model.impl;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpacesCustomOrder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class SpacesCustomOrder extends VSpaceElement implements ISpacesCustomOrder {
    
    @Id
    @GeneratedValue(generator = "customorder_id_generator")
    @GenericGenerator(name = "customorder_id_generator",
        parameters = @Parameter(name = "prefix", value = "SPC"),
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @Column(nullable=false)
    private String customOrderName;

    @ManyToMany(targetEntity = Space.class)
    @JoinTable(name = "spaces_custom_orders",
               joinColumns = @JoinColumn(name = "SpacesCustomOrder_Id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name = "Space_Id", referencedColumnName="id"))
    @OrderColumn
    private List<ISpace> customOrderedSpaces;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCustomOrderName() {
        return customOrderName;
    }

    public void setCustomOrderName(String customOrderName) {
        this.customOrderName = customOrderName;
    }
    
    public List<ISpace> getCustomOrderedSpaces() {
        return customOrderedSpaces;
    }

    public void setCustomOrderedSpaces(List<ISpace> customOrderedSpaces) {
        this.customOrderedSpaces = customOrderedSpaces;
    }

}
