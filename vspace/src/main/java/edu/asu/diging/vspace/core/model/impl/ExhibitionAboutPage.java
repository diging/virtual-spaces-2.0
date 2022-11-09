package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IExhibitionDescription;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.impl.AboutPageData;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Model for Exhibition About Page
 * 
 * @author Avirup Biswas
 *
 */
@Entity
public class ExhibitionAboutPage {
    @Id
    @GeneratedValue(generator = "exh_abtpg_id_generator")
    @GenericGenerator(name = "exh_abtpg_id_generator", parameters = @Parameter(name = "prefix", value = "EXHABT"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @Lob
    private String title;
    
    @Lob
    private String aboutPageText;
    
    
    @OneToMany(mappedBy = "userText", targetEntity = LanguageDescriptionObject.class)
    private List<ILanguageDescriptionObject> exhibitionTitles;

    @OneToMany(mappedBy = "userText", targetEntity = LanguageDescriptionObject.class)
    private List<ILanguageDescriptionObject> exhibitionTextDescriptions;


    public List<ILanguageDescriptionObject> getExhibitionTitles() {
		return exhibitionTitles;
	}

	public void setExhibitionTitles(List<ILanguageDescriptionObject> exhibitionTitles) {
		this.exhibitionTitles = exhibitionTitles;
	}

	public List<ILanguageDescriptionObject> getExhibitionTextDescriptions() {
		return exhibitionTextDescriptions;
	}

	public void setExhibitionTextDescriptions(List<ILanguageDescriptionObject> exhibitionTextDescriptions) {
		this.exhibitionTextDescriptions = exhibitionTextDescriptions;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAboutPageText() {
        return aboutPageText;
    }
    
    
    public void setTitle(String title) {
		this.title = title;
	}

	public void setAboutPageText(String aboutPageText) {
		this.aboutPageText = aboutPageText;
	}


	/*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ITextBlock#htmlRenderedText()
     */
    public String htmlRenderedText() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(aboutPageText);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
