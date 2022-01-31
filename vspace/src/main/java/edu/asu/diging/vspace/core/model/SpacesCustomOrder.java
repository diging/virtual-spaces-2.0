package edu.asu.diging.vspace.core.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.impl.Module;
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
    
    @Column(unique=true)
    private String customOrderName;
    
    public String getCustomOrderName() {
        return customOrderName;
    }

    public void setCustomOrderName(String customOrderName) {
        this.customOrderName = customOrderName;
    }

    @ManyToMany(mappedBy = "spacesCustomOrder", targetEntity = Space.class)
    private List<ISpace> customOrderedSpaces;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    public List<ISpace> getCustomOrderedSpaces() {
        return customOrderedSpaces;
    }

    public void setCustomOrderedSpaces(List<ISpace> customOrderedSpaces) {
        this.customOrderedSpaces = customOrderedSpaces;
    }


}
