package edu.asu.diging.vspace.core.model.impl;

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
import edu.asu.diging.vspace.core.model.IVSpaceElement;

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
public class ExhibitionAboutPage extends VSpaceElement{
    @Id
    @GeneratedValue(generator = "exh_abtpg_id_generator")
    @GenericGenerator(name = "exh_abtpg_id_generator", parameters = @Parameter(name = "prefix", value = "EXHABT"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @Lob
    private String title;
    
    @Lob
    private String aboutPageText;
    
    @OneToMany(mappedBy = "userText", targetEntity = LanguageDescriptionObject.class)
    private List<LanguageDescriptionObject> exhibitionTitles;

    @OneToMany(mappedBy = "userText", targetEntity = LanguageDescriptionObject.class)
    private List<LanguageDescriptionObject> exhibitionTextDescriptions;


    public List<LanguageDescriptionObject> getExhibitionTitles() {
		return exhibitionTitles;
	}

	public void setExhibitionTitles(List<LanguageDescriptionObject> exhibitionTitles) {
		this.exhibitionTitles = exhibitionTitles;
	}

	public List<LanguageDescriptionObject> getExhibitionTextDescriptions() {
		return exhibitionTextDescriptions;
	}

	public void setExhibitionTextDescriptions(List<LanguageDescriptionObject> exhibitionTextDescriptions) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAboutPageText() {
        return aboutPageText;
    }

    public void setAboutPageText(String aboutPageText) {
        this.aboutPageText = aboutPageText;
    }
    
    @Override 
    public void setDescription(String description) {             
    	LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
        languageObject.setUserText(description);    
        languageObject.setExhibitionLanguage(null);       
        this.getExhibitionTextDescriptions().add(languageObject);
    }
    
    @Override 
    public void setName(String title) {
    	LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
        languageObject.setUserText(title);    
        languageObject.setExhibitionLanguage(null);       
        this.getExhibitionTitles().add(languageObject);
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
