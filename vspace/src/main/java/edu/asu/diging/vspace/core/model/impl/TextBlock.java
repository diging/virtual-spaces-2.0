package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import edu.asu.diging.vspace.core.model.ITextBlock;

@Entity
public class TextBlock extends ContentBlock implements ITextBlock {

    @Lob
    private String text;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ITextBlock#getText()
     */
    @Override
    public String getText() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.ITextBlock#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

}
