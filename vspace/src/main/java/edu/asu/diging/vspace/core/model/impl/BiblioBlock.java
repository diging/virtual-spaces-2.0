package edu.asu.diging.vspace.core.model.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
        return "title=" + title + ", author=" + author + ", year=" + year + ", journal=" + journal
                + ", url=" + url + ", volume=" + volume + ", issue=" + issue + ", pages=" + pages + ", editors="
                + editors + ", type=" + type + ", note=" + note ;
    }
    
    @Transient
    public String htmlRenderedBiblio() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(toString());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    } 
    
    @Transient
    public String urlEncodedBiblioMetaData() throws UnsupportedEncodingException {
        
        String urlEncodedBiblioMetaData = "url_ver=Z39.88-2004&ctx_ver=Z39.88-2004&rfr_id=info%3Asid%2Fzotero.org%3A2&rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Adissertation";
            if(title!=null)
                urlEncodedBiblioMetaData += "&rft.title=" + URLEncoder.encode(title, StandardCharsets.UTF_8.toString()); 
            
            if(author!=null)
                urlEncodedBiblioMetaData += "&rft.au=" + URLEncoder.encode(author, StandardCharsets.UTF_8.toString()); 
            
            if(year!=null)
                urlEncodedBiblioMetaData += "&rft.date=" + URLEncoder.encode(year, StandardCharsets.UTF_8.toString());
            
            if(journal!=null)
                urlEncodedBiblioMetaData += "&rft.journal=" + URLEncoder.encode(journal, StandardCharsets.UTF_8.toString());
            
            if(url!=null)
                urlEncodedBiblioMetaData += "&rft.identifier=" + URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
            
            if(volume!=null)
                urlEncodedBiblioMetaData += "&rft.volume=" + URLEncoder.encode(volume, StandardCharsets.UTF_8.toString());
            
            if(issue!=null)
                urlEncodedBiblioMetaData += "&rft.issue=" + URLEncoder.encode(issue, StandardCharsets.UTF_8.toString());
            
            if(pages!=null)
                urlEncodedBiblioMetaData += "&rft.pages=" + URLEncoder.encode(pages, StandardCharsets.UTF_8.toString());
            
            if(editors!=null)
                urlEncodedBiblioMetaData += "&rft.editors=" + URLEncoder.encode(editors, StandardCharsets.UTF_8.toString());
            
            if(type!=null)
                urlEncodedBiblioMetaData += "&rft.type=" + URLEncoder.encode(type, StandardCharsets.UTF_8.toString());
            
            if(note!=null)
                urlEncodedBiblioMetaData += "&rft.note=" + URLEncoder.encode(note, StandardCharsets.UTF_8.toString());
            
            urlEncodedBiblioMetaData += "&rft.language=en";
                
                
//        urlEncodedBiblioMetaData = "url_ver=Z39.88-2004&ctx_ver=Z39.88-2004&rfr_id=info%3Asid%2Fzotero.org%3A2&rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Adissertation&rft.title=La%20digitalisation%20des%20points%20de%20vente%20%3A%20compl%C3%A9mentarit%C3%A9%20entre%20les%20boutiques%20physiques%20et%20les%20boutiques%20connect%C3%A9es&rft.inst=IDRAC&rft.degree=Th%C3%A8se%20professionnelle&rft.au=KHETTAL%2C%20Sarah&rft.date=03%2F11%2F2016&rft.place=Lyon&rft.language=Fran%C3%A7ais&rft.tpages=91%20p%2E&rft.identifier=http://infotheque.campushep-lyon.net/Record.htm?record=19218093124910362759";
        return urlEncodedBiblioMetaData;
    }

}
