package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String title;
    
    private String author;
    
    private Integer year;
    
    private String journal;
    

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IBiblioBlock#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IBiblioBlock#setTitle(java.lang.String)
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Transient
    public String htmlRenderedText() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(title);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String getJournal() {
        return journal;
    }

    @Override
    public void setJournal(String journal) {
        this.journal = journal;
    }

}
