package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import edu.asu.diging.vspace.core.model.IExhibitionDescription;



@Entity
public class ExhibitionDescription extends VSpaceElement implements IExhibitionDescription{
	
	@Id
    @GeneratedValue(generator = "exhibition_description_id_generator")
    @GenericGenerator(name = "exhibition_description_id_generator", parameters = @Parameter(name = "prefix", value = "EXHDES"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = ExhibitionLanguage.class)
    private ExhibitionLanguage exhibitionLanguage; 

    String description;

    @ManyToOne(targetEntity = ExhibitionAboutPage.class)
    private ExhibitionAboutPage exhibitionAboutPage;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }

    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }

}
