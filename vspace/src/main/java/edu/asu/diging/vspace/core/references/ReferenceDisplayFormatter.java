package edu.asu.diging.vspace.core.references;

public class ReferenceDisplayFormatter {
    
    private StringBuilder referenceDisplayText;
    
    public ReferenceDisplayFormatter() {
        referenceDisplayText = new StringBuilder();
    }

    public String getReferenceDisplayText() {
        return referenceDisplayText.toString();
    }

    public ReferenceDisplayFormatter addAuthors(String author) {
        if(author!=null && !author.equals("")) {
            this.referenceDisplayText.append(author + ". ");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addYear(String year) {
        if(year!=null && !year.equals("")) {
            this.referenceDisplayText.append("("+ year + "). ");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addTitle(String title) {
        if(title!=null && !title.equals("")) {
            this.referenceDisplayText.append(title + ". ");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addJournal(String journal) {
        if(journal!=null && !journal.equals("")) {
            this.referenceDisplayText.append("In: " + journal + ". ");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addPages(String pages) {
        if(pages!=null && !pages.equals("")) {
            this.referenceDisplayText.append("p. " + pages + ". ");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addVolume(String volume) {
        if(volume!=null && !volume.equals("")) {
            this.referenceDisplayText.append(volume + ". ");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addIssue(String issue) {
        if(issue!=null && !issue.equals("")) {
            this.referenceDisplayText.append("("+ issue + ").");
        }
        return this;
    }
    
    public ReferenceDisplayFormatter addUrl(String url) {
        if(url!=null && !url.equals("")) {
            this.referenceDisplayText.append(url);
        }
        return this;
    }
    
}
