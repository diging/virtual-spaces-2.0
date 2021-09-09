package edu.asu.diging.vspace.references;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
public class ReferenceDisplayProvider {
    
    public String getReferenceDisplayText(Reference reference) {
        
        String referenceDisplayText = "";
        
        String type = reference.getType();
        String title = reference.getTitle();
        String author = reference.getAuthor();
        String year = reference.getYear();
        String journal = reference.getJournal();
        String url = reference.getUrl();
        String volume = reference.getVolume();
        String issue = reference.getIssue();
        String pages = reference.getPages();
        String editors = reference.getEditors();
        String note = reference.getNote();
        
        if(author!=null && !author.equals("")) {
            referenceDisplayText += author + " ";
        }
        
        if(year!=null && !year.equals("")) {
            referenceDisplayText += "("+ year + "). ";
        }
        
        if(title!=null && !title.equals("")) {
            referenceDisplayText += title + ". ";
        }
        
        if(journal!=null && !journal.equals("")) {
            referenceDisplayText += "In: " + journal + ". ";
        }
        
        if(pages!=null && !pages.equals("")) {
            referenceDisplayText += pages + ". ";
        }
        
        if(volume!=null && !volume.equals("")) {
            referenceDisplayText += volume + " ";
        }
        
        if(issue!=null && !issue.equals("")) {
            referenceDisplayText += "("+ issue + ").";
        }
        
        if(url!=null && !url.equals("")) {
            referenceDisplayText += url;
        }
        
        return referenceDisplayText;
    }
    
}
