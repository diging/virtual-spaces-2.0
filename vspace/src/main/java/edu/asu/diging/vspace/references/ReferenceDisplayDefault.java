package edu.asu.diging.vspace.references;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
public class ReferenceDisplayDefault implements ReferenceDisplayProvider{
    
    public String getReferenceDisplayText(Reference reference) {
        
        String referenceDisplayText = "";
        String title = reference.getTitle();
        String author = reference.getAuthor();
        String year = reference.getYear();
        String journal = reference.getJournal();
        String url = reference.getUrl();
        String volume = reference.getVolume();
        String issue = reference.getIssue();
        String pages = reference.getPages();
        
        referenceDisplayText += getRefAuthorDisplayText(author) + getRefYearDisplayText(year) + getRefTitleDisplayText(title)
            + getRefJournalDisplayText(journal) + getRefPagesDisplayText(pages) + getRefVolumeDisplayText(volume) + getRefIssueDisplayText(issue)
            + getRefUrlDisplayText(url);
        
        return referenceDisplayText;
    }
    
    String getRefAuthorDisplayText(String author) {
        if(author==null || author.equals("")) {
            return "";
        }
        
        return author + " ";
    }
    
    String getRefYearDisplayText(String year) {
        if(year==null || year.equals("")) {
            return "";
        }
        
        return "("+ year + "). ";
    }
    
    String getRefTitleDisplayText(String title) {
        if(title==null || title.equals("")) {
            return "";
        }
        
        return title + ". ";
    }
    
    String getRefJournalDisplayText(String journal) {
        if(journal==null || journal.equals("")) {
            return "";
        }
        
        return "In: " + journal + ". ";
    }
    
    String getRefPagesDisplayText(String pages) {
        if(pages==null || pages.equals("")) {
            return "";
        }
        
        return pages + ". ";
    }
    
    String getRefVolumeDisplayText(String volume) {
        if(volume==null || volume.equals("")) {
            return "";
        }
        
        return volume + " ";
    }
    
    String getRefIssueDisplayText(String issue) {
        if(issue==null || issue.equals("")) {
            return "";
        }
        
        return "("+ issue + ").";
    }
    
    String getRefUrlDisplayText(String url) {
        if(url==null || url.equals("")) {
            return "";
        }
        
        return url;
    }
    
}
