package edu.asu.diging.vspace.core.references.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.references.IReferenceDisplayFormatter;
import edu.asu.diging.vspace.core.references.IReferenceDisplayProvider;
import edu.asu.diging.vspace.core.references.ReferenceDisplayType;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

@Service
public class ReferenceDisplayDefault implements IReferenceDisplayProvider {

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
            
        
        refFormatter.addMetadata(null, type, ". ")
        .addMetadata(null, author, ". ")
        .addMetadata("(" , year, ")")
        .addMetadata(null, title, ". ")
        .addMetadata("In: " , journal, ". ")
        .addMetadata("p. ", pages, ". ")
        .addMetadata(null, volume, ". ")
        .addMetadata("(", issue, ")")
        .addMetadata(null, url, null)
        .addMetadata(null, abstracts, null)
        .addMetadata(null, companyName, null);

        return refFormatter.getReferenceDisplayText();
    }

    @Override
    public ReferenceDisplayType getReferenceDisplayType() {
        return ReferenceDisplayType.DEFAULT;
    }

}
