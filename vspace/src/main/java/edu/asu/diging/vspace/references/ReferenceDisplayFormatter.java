package edu.asu.diging.vspace.references;

public class ReferenceDisplayFormatter {
    
    String referenceDisplayText;
    
    public ReferenceDisplayFormatter() {
        referenceDisplayText = "";
    }

    
    public ReferenceDisplayFormatter addAuthors(String author) {
        if(author!=null && !author.equals("")) {
            this.referenceDisplayText += author + " ";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addYear(String year) {
        if(year!=null && !year.equals("")) {
            this.referenceDisplayText +="("+ year + "). ";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addTitle(String title) {
        if(title!=null && !title.equals("")) {
            this.referenceDisplayText +=title + ". ";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addJournal(String journal) {
        if(journal!=null && !journal.equals("")) {
            this.referenceDisplayText +="In: " + journal + ". ";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addPages(String pages) {
        if(pages!=null && !pages.equals("")) {
            this.referenceDisplayText += ". ";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addVolume(String volume) {
        if(volume!=null && !volume.equals("")) {
            this.referenceDisplayText += volume + " ";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addIssue(String issue) {
        if(issue!=null && !issue.equals("")) {
            this.referenceDisplayText += "("+ issue + ").";
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addUrl(String url) {
        if(url!=null && !url.equals("")) {
            this.referenceDisplayText += url;
        }
        return this;
    }
}
