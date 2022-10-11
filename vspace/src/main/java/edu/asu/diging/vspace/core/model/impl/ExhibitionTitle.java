package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

import edu.asu.diging.vspace.core.model.IExhibitionTitle;

@Entity
public class ExhibitionTitle extends VSpaceElement implements IExhibitionTitle{

	@Id
    @GeneratedValue(generator = "exhibition_title_id_generator")
    @GenericGenerator(name = "exhibition_title_id_generator", parameters = @Parameter(name = "prefix", value = "EXBTTL"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @OneToOne(targetEntity = ExhibitionLanguage.class)
    private ExhibitionLanguage exhibitionLanguage; 

    private String title;

    @ManyToOne(targetEntity = ExhibitionAboutPage.class)
    private ExhibitionAboutPage exhibitionAboutPage;


    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }

    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }


}
