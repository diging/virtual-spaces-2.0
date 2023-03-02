package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.references.impl.ReferenceDisplayFormatter;

public interface IReferenceDisplayFormatter {
    
    public String getReferenceDisplayText();
    
    public ReferenceDisplayFormatter addAuthors(String author);
    
    public ReferenceDisplayFormatter addYear(String year);
    
    public ReferenceDisplayFormatter addTitle(String title);
    
    public ReferenceDisplayFormatter addType(String type);
    
    public ReferenceDisplayFormatter addJournal(String journal);
    
    public ReferenceDisplayFormatter addPages(String pages);
    
    public ReferenceDisplayFormatter addVolume(String volume);
    
    public ReferenceDisplayFormatter addIssue(String issue);
    
    public ReferenceDisplayFormatter addUrl(String url);
    
    public ReferenceDisplayFormatter addAbstracts(String abstracts);
     
    public ReferenceDisplayFormatter addCompanyName(String companyName);

}
