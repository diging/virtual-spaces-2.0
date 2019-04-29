package edu.asu.diging.vspace.core.model.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;

@Entity
public class Slide extends VSpaceElement implements ISlide {

    @Id
    @GeneratedValue(generator = "slide_id_generator")
    @GenericGenerator(name = "slide_id_generator", 
        parameters = @Parameter(name = "prefix", value = "SLI"),
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = Module.class)
    private IModule module;

    @OneToMany(targetEntity = ContentBlock.class, mappedBy = "slide", cascade = CascadeType.ALL)
    private List<IContentBlock> contents;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getModule()
     */
    @Override
    public IModule getModule() {
        return module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISlide#setImage(edu.asu.diging.
     * vspace. core.model.IModule)
     */
    @Override
    public void setModule(IModule module) {
        this.module = module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISlide#getContents()
     */
    @Override
    public List<IContentBlock> getContents() {
        Collections.sort(this.contents, new Comparator<IContentBlock>() {

            @Override
            public int compare(IContentBlock o1, IContentBlock o2) {
                return o1.getContentOrder().compareTo(o2.getContentOrder());
            }
        });
        return contents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ISlide#setContents(edu.asu.diging.
     * vspace. core.model.IContentBlock)
     */
    @Override
    public void setContents(List<IContentBlock> contents) {
        this.contents = contents;
    }

}
