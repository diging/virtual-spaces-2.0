package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Model for ExhibitionAbout
 * 
 * @author abiswa15
 *
 */
@Entity
public class ExhibitionAboutPage {
    @Id
    @GeneratedValue(generator = "exh_abtpg_id_generator")
    @GenericGenerator(name = "exh_abtpg_id_generator", parameters = @Parameter(name = "prefix", value = "EXHABT"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    private String title;

    private String aboutPageText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAboutPageText() {
        return aboutPageText;
    }

    public void setAboutPageText(String aboutPageText) {
        this.aboutPageText = aboutPageText;
    }

}
