package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ILocalizedText;

/**
 * Model for Exhibition About Page
 * 
 * @author Avirup Biswas
 *
 */
@Entity
public class ExhibitionAboutPage extends VSpaceElement {
    @Id
    @GeneratedValue(generator = "exh_abtpg_id_generator")
    @GenericGenerator(name = "exh_abtpg_id_generator", parameters = @Parameter(name = "prefix", value = "EXHABT"), strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    
    @Lob
    private String title;
    
    @Lob
    private String aboutPageText;
    
    
    
    @OneToMany(mappedBy= "targetElement", targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="AboutPage_LocText_titles")
    private List<ILocalizedText> localizedTitles = new ArrayList<ILocalizedText>();

    @OneToMany(mappedBy= "targetElement", targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="AboutPage_LocText_descriptions")
    private List<ILocalizedText> localizedDescriptions = new ArrayList<ILocalizedText>();

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
	
    public List<ILocalizedText> getExhibitionTitles() {
        return localizedTitles;
    }

    public void setExhibitionTitles(List<ILocalizedText> exhibitionTitles) {
        this.localizedTitles = exhibitionTitles;
    }

    public List<ILocalizedText> getExhibitionTextDescriptions() {
        return localizedDescriptions;
    }

    public void setExhibitionTextDescriptions(List<ILocalizedText> exhibitionTextDescriptions) {
        this.localizedDescriptions = exhibitionTextDescriptions;
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
