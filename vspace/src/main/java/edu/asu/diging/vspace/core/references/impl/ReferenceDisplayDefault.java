package edu.asu.diging.vspace.core.references.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.references.IReferenceDisplayFormatter;
import edu.asu.diging.vspace.core.references.IReferenceDisplayProvider;

@Service
public class ReferenceDisplayDefault implements IReferenceDisplayProvider{
    
    public String getReferenceDisplayText(IReference reference) {
        String title = reference.getTitle();
        String type = reference.getType();
        String author = reference.getAuthor();
        String year = reference.getYear();
        String journal = reference.getJournal();
        String url = reference.getUrl();
        String volume = reference.getVolume();
        String issue = reference.getIssue();
        String pages = reference.getPages();
        String abstracts = reference.getAbstracts();
        String companyName = reference.getCompanyName();
        IReferenceDisplayFormatter refFormatter = new ReferenceDisplayFormatter();
        refFormatter.addType(
                type).addAuthors(author).addYear(year).addTitle(title).addJournal(journal).addPages(pages).addVolume(volume).addIssue(issue).addUrl(url).addAbstracts(abstracts).addCompanyName(companyName);
        
        return refFormatter.getReferenceDisplayText();
    }
    
}
