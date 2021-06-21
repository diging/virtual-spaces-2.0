package edu.asu.diging.vspace.core.model.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import edu.asu.diging.vspace.biblioExpose.BiblioContext;
import edu.asu.diging.vspace.biblioExpose.CitationStyleDefault;
import edu.asu.diging.vspace.core.model.IBiblioBlock;

@Entity
public class BiblioBlock extends ContentBlock implements IBiblioBlock {

    private String title;

    private String author;

    private String year;

    private String journal;

    private String url;

    private String volume;

    private String issue;

    private String pages;

    private String editors;

    private String type;

    private String note;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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
    public String getYear() {
        return year;
    }

    @Override
    public void setYear(String year) {
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

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getVolume() {
        return volume;
    }

    @Override
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String getIssue() {
        return issue;
    }

    @Override
    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Override
    public String getPages() {
        return pages;
    }

    @Override
    public void setPages(String pages) {
        this.pages = pages;
    }

    @Override
    public String getEditors() {
        return editors;
    }

    @Override
    public void setEditors(String editors) {
        this.editors = editors;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    @Transient
    public String toString() {
        return "title=" + title + ", author=" + author + ", year=" + year + ", journal=" + journal + ", url=" + url
                + ", volume=" + volume + ", issue=" + issue + ", pages=" + pages + ", editors=" + editors + ", type="
                + type + ", note=" + note;
    }

    @Transient
    public String htmlRenderedBiblio() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(toString());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    @Transient
    public String urlEncodedBiblioMetaData() {
        BiblioContext biblioContext = new BiblioContext(new CitationStyleDefault(), this);
        biblioContext.executeBiblioMetadata(this);
        
    }

}
