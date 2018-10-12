package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;

/**
 * Represents an exhibition that can have a default start space.
 * @see edu.asu.diging.vspace.core.model.IExhibition
 * @author Namratha
 */
@Entity
public class Exhibition extends VSpaceElement implements IExhibition {

  @Id
  @GeneratedValue(generator = "exhibit-id-generator")
  @GenericGenerator(name = "exhibit-id-generator",
      parameters = @Parameter(name = "prefix", value = "EXH"),
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
  private String id;

  @OneToOne(targetEntity = Space.class)
  private ISpace startSpace;

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
  public ISpace getSpace() {
    return this.startSpace;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.asu.diging.vspace.core.model.IExhibition#setSpace(edu.asu.diging.vspace.
   * core.model.ISpace)
   */
  @Override
  public void setSpace(ISpace space) {
    this.startSpace = space;
  }

}
