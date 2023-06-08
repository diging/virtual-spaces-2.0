package edu.asu.diging.vspace.core.model.impl;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.ISpace;


@Entity
public class LocalizedText implements ILocalizedText {

    @Id
    @GeneratedValue(generator = "localized_text_id_generator")
    @GenericGenerator(name = "localized_text_id_generator", parameters = @Parameter(name = "prefix", value = "LOCTEXT"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "LOC_EXH_LANG")
    private ExhibitionLanguage exhibitionLanguage;
    
    @ManyToOne( targetEntity = Space.class)
    ISpace targetSpace;

    private String text;

    public LocalizedText() {
        super();
    }

    public LocalizedText(ExhibitionLanguage exhibitionLanguage, String text) {
        super();
        this.exhibitionLanguage = exhibitionLanguage;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExhibitionLanguage getExhibitionLanguage() {
        return exhibitionLanguage;
    }

    public void setExhibitionLanguage(ExhibitionLanguage exhibitionLanguage) {
        this.exhibitionLanguage = exhibitionLanguage;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ISpace getTargetSpace() {
        return targetSpace;
    }

    public void setTargetSpace(ISpace space) {
        this.targetSpace = space;
    }

}