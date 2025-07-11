package edu.asu.diging.vspace.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.vspace.core.model.IReference;

@Entity
public class Reference extends VSpaceElement implements IReference {
    @Id
    @GeneratedValue(generator = "reference_id_generator")
    @GenericGenerator(name = "reference_id_generator", 
        parameters = @Parameter(name = "prefix", value = "REF"),
        strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
    private String id;
    @ManyToMany
    @JoinTable(name = "Biblio_Reference", joinColumns = @JoinColumn(name = "REFERENCE_ID"), inverseJoinColumns = @JoinColumn(name = "BIBLIO_ID"))
    private List<BiblioBlock> biblios = new ArrayList<>();

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
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public List<BiblioBlock> getBiblios() {
        return biblios;
    }

    public void setBiblios(List<BiblioBlock> biblios) {
        this.biblios = biblios;
    }
    
}
