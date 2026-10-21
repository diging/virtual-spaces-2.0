package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.ITextBlock;

@Entity
public class TextBlock extends ContentBlock implements ITextBlock {

    @Lob
    private String text;
    
    @OneToMany(targetEntity = LocalizedText.class, cascade={CascadeType.ALL})
    @JoinTable(name="TextBlock_LangObj_texts",  
        joinColumns = @JoinColumn(name = "TextBlock_Id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name = "LocalizedText_Id", referencedColumnName="id"))
    private List<ILocalizedText> localizedTexts = new ArrayList<ILocalizedText>();

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    @Transient
    public String htmlRenderedText() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text != null ? text : "");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
    
    @Override
    public List<ILocalizedText> getLocalizedTexts() {
        return localizedTexts;
    }

    @Override
    public void setLocalizedTexts(List<ILocalizedText> localizedTexts) {
        this.localizedTexts = localizedTexts;
    }
    
    @Override
    public String getLocalizedText(String languageCode, String defaultLanguageCode) {
        if (localizedTexts == null || localizedTexts.isEmpty()) {
            return text != null ? text : "";
        }
        
        // First, try to find text in the requested language
        if (languageCode != null && !languageCode.isEmpty()) {
            for (ILocalizedText localizedText : localizedTexts) {
                if (localizedText.getExhibitionLanguage() != null && 
                    languageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        // If not found, try default language
        if (defaultLanguageCode != null && !defaultLanguageCode.isEmpty() && !defaultLanguageCode.equals(languageCode)) {
            for (ILocalizedText localizedText : localizedTexts) {
                if (localizedText.getExhibitionLanguage() != null && 
                    defaultLanguageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        // Last resort: return the first available localized text or original text
        for (ILocalizedText localizedText : localizedTexts) {
            if (localizedText.getText() != null && !localizedText.getText().trim().isEmpty()) {
                return localizedText.getText();
            }
        }
        
        return text != null ? text : "";
    }
    
    @Override
    public String getLocalizedHtmlRenderedText(String languageCode, String defaultLanguageCode) {
        String localizedText = getLocalizedText(languageCode, defaultLanguageCode);
        if (localizedText != null && !localizedText.isEmpty()) {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(localizedText);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            return renderer.render(document);
        }
        return "";
    }
}
