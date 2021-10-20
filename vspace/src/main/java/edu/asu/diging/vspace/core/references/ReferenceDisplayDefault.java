package edu.asu.diging.vspace.core.references;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
public class ReferenceDisplayDefault implements ReferenceDisplayProvider{
    
    public String getReferenceDisplayText(Reference reference) {
        
        String title = reference.getTitle();
        String author = reference.getAuthor();
        String year = reference.getYear();
        String journal = reference.getJournal();
        String url = reference.getUrl();
        String volume = reference.getVolume();
        String issue = reference.getIssue();
        String pages = reference.getPages();
        
        ReferenceDisplayFormatter refFormatter = new ReferenceDisplayFormatter();
        refFormatter.addAuthors(author).addYear(year).addTitle(title).addJournal(journal).addPages(pages).addVolume(volume).addIssue(issue).addUrl(url);
        
        return refFormatter.getReferenceDisplayText();
    }
    
}
