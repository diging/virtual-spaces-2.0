package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

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
    
    @OneToMany(targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="AboutPage_LocText_titles",  
        joinColumns = @JoinColumn(name = "ExhibitionAboutPage_Id", referencedColumnName="id")
    )
    private List<ILocalizedText> localizedTitles = new ArrayList<ILocalizedText>();

    @OneToMany(targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="AboutPage_LocText_descriptions",
        joinColumns = @JoinColumn(name = "ExhibitionAboutPage_Id", referencedColumnName="id")
    )
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
        return renderMarkdown(aboutPageText);
    }

    /**
     * Returns the title in the language with the given code, or the default title
     * if there is no title in that language.
     */
    public String getTitleForLanguage(String languageCode) {
        return getTextForLanguage(localizedTitles, languageCode, title);
    }

    /**
     * Renders the about page text in the language with the given code as html, or the
     * default text if there is no text in that language.
     */
    public String htmlRenderedText(String languageCode) {
        return renderMarkdown(getTextForLanguage(localizedDescriptions, languageCode, aboutPageText));
    }

    private String getTextForLanguage(List<ILocalizedText> localizedTexts, String languageCode, String defaultText) {
        if (languageCode == null || localizedTexts == null) {
            return defaultText;
        }
        return localizedTexts.stream()
                .filter(text -> text != null && text.getExhibitionLanguage() != null
                        && languageCode.equals(text.getExhibitionLanguage().getCode())
                        && text.getText() != null && !text.getText().isBlank())
                .map(ILocalizedText::getText)
                .findFirst()
                .orElse(defaultText);
    }

    private String renderMarkdown(String text) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text != null ? text : "");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}