package edu.asu.diging.vspace.core.model.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.text.StringEscapeUtils;
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
        
        
        
        String urlEncodedBiblioMetaData = "url_ver=Z39.88-2004&ctx_ver=Z39.88-2004";
        
        String ref_id = "info:sid/zotero.org:2";
        urlEncodedBiblioMetaData += "&rfr_id=" + URLEncoder.encode(ref_id, StandardCharsets.UTF_8.toString()); 
        
        String rft_val_fmt = "info:ofi/fmt:kev:mtx:dissertation";
        urlEncodedBiblioMetaData += "&rft_val_fmt=" + URLEncoder.encode(rft_val_fmt, StandardCharsets.UTF_8.toString());
        
        
//        urlEncodedBiblioMetaData = "url_ver=Z39.88-2004&ctx_ver=Z39.88-2004&rfr_id=info:sid/zotero.org:2&rft_val_fmt=info:ofi/fmt:kev:mtx:dissertation";
//        System.out.println(StringEscapeUtils.escapeHtml4(urlEncodedBiblioMetaData));
//        urlEncodedBiblioMetaData = StringEscapeUtils.escapeHtml4(urlEncodedBiblioMetaData);
        
        if(title!=null)
            urlEncodedBiblioMetaData += ("&rft.title=") + URLEncoder.encode(title, StandardCharsets.UTF_8.toString()); 
        
        if(author!=null)
            urlEncodedBiblioMetaData += ("&rft.au=") + URLEncoder.encode(author, StandardCharsets.UTF_8.toString()); 
        
        if(year!=null)
            urlEncodedBiblioMetaData += ("&rft.date=") + URLEncoder.encode(year, StandardCharsets.UTF_8.toString());
        
        if(journal!=null)
            urlEncodedBiblioMetaData += ("&rft.jtitle=") + URLEncoder.encode(journal, StandardCharsets.UTF_8.toString());
        
        if(url!=null)
            urlEncodedBiblioMetaData += ("&rft_id=") + URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        
        if(volume!=null)
            urlEncodedBiblioMetaData += ("&rft.volume=") + URLEncoder.encode(volume, StandardCharsets.UTF_8.toString());
        
        if(issue!=null)
            urlEncodedBiblioMetaData += ("&rft.issue=") + URLEncoder.encode(issue, StandardCharsets.UTF_8.toString());
        
        if(pages!=null)
            urlEncodedBiblioMetaData += ("&rft.pages=") + URLEncoder.encode(pages, StandardCharsets.UTF_8.toString());
        
        if(editors!=null)
            urlEncodedBiblioMetaData += ("&rft.editors=") + URLEncoder.encode(editors, StandardCharsets.UTF_8.toString());
        
        if(type!=null)
            urlEncodedBiblioMetaData += ("&rft.type=") + URLEncoder.encode(type, StandardCharsets.UTF_8.toString());
        
        if(note!=null)
            urlEncodedBiblioMetaData += ("&rft.note=") + URLEncoder.encode(note, StandardCharsets.UTF_8.toString());
        
        urlEncodedBiblioMetaData += ("&rft.language=en");
        
//        urlEncodedBiblioMetaData = StringEscapeUtils.escapeHtml4(urlEncodedBiblioMetaData);
        System.out.println(urlEncodedBiblioMetaData);
        
        return urlEncodedBiblioMetaData;
    }

}
