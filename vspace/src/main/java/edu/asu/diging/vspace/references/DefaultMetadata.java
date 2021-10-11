package edu.asu.diging.vspace.references;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException;
import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
@PropertySource({"classpath:config_reference.properties"})
public class DefaultMetadata implements ReferenceMetadataProvider {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private Environment env;
    
    @Override
    public ReferenceMetadataType getReferenceMetadataType() {
        return ReferenceMetadataType.DEFAULT;
    }

    @Override
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException {
        String urlEncodedReferenceMetaData = ReferenceConstants.DEFAULT_URL_VERSION + "&" + ReferenceConstants.DEFAULT_CTX_VERSION;
        try {
            urlEncodedReferenceMetaData += ReferenceConstants.RFR_ID_TAG + URLEncoder.encode(ReferenceConstants.DEFAULT_RFR_ID, StandardCharsets.UTF_8.name());
            urlEncodedReferenceMetaData += ReferenceConstants.RFT_VAL_FMT_TAG
                    + URLEncoder.encode(ReferenceConstants.DEFAULT_RFT_VAL_FMT, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }

        String type = env.getProperty(reference.getType());
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

        urlEncodedReferenceMetaData += getRefTypeEncoded(type) + getRefTitleEncoded(title) + getRefAuthorEncoded(author) + getRefYearEncoded(year)
            + getRefJournalEncoded(journal) + getRefUrlEncoded(url) + getRefVolumeEncoded(volume) + getRefIssueEncoded(issue) + getRefPagesEncoded(pages)
            + getRefEditorsEncoded(editors) + getRefNoteEncoded(note);

        urlEncodedReferenceMetaData += ReferenceConstants.RFT_DEFAULT_LANGUAGE;

        return urlEncodedReferenceMetaData;
    }
    
    private String getRefTypeEncoded(String type) throws ReferenceMetadataEncodingException {
        
        if(type==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_DEGREE_TAG + URLEncoder.encode(type, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefTitleEncoded(String title) throws ReferenceMetadataEncodingException {
        
        if(title==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_TITLE_TAG + URLEncoder.encode(title, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefAuthorEncoded(String author) throws ReferenceMetadataEncodingException {
        
        if(author==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_AUTHOR_TAG + URLEncoder.encode(author, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefYearEncoded(String year) throws ReferenceMetadataEncodingException {
        
        if(year==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_DATE_TAG + URLEncoder.encode(year, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefJournalEncoded(String journal) throws ReferenceMetadataEncodingException {
        
        if(journal==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_JOURNAL_TAG + URLEncoder.encode(journal, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefUrlEncoded(String url) throws ReferenceMetadataEncodingException {
        
        if(url==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_ID_TAG + URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefVolumeEncoded(String volume) throws ReferenceMetadataEncodingException {
        
        if(volume==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_VOLUME_TAG + URLEncoder.encode(volume, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefIssueEncoded(String issue) throws ReferenceMetadataEncodingException {
        
        if(issue==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_ISSUE_TAG + URLEncoder.encode(issue, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefPagesEncoded(String pages) throws ReferenceMetadataEncodingException {
        String refPagesEncoded = "";
        if (pages != null) {
            try {
                refPagesEncoded += ReferenceConstants.RFT_ISSUE_TAG
                        + URLEncoder.encode(pages, StandardCharsets.UTF_8.name());
                String[] tokens = pages.split("-", 2);
                if (tokens.length > 1) {
                    refPagesEncoded += ReferenceConstants.RFT_START_PAGE_TAG
                            + URLEncoder.encode(tokens[0], StandardCharsets.UTF_8.name());
                    refPagesEncoded += ReferenceConstants.RFT_END_PAGE_TAG
                            + URLEncoder.encode(tokens[1], StandardCharsets.UTF_8.name());
                }
            } catch (UnsupportedEncodingException e) {
                throw new ReferenceMetadataEncodingException(e);
            }
        }
        return refPagesEncoded;
    }
    
    private String getRefEditorsEncoded(String editors) throws ReferenceMetadataEncodingException {
        
        if(editors==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_EDITOR_TAG + URLEncoder.encode(editors, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefNoteEncoded(String note) throws ReferenceMetadataEncodingException {
        
        if(note==null) {
            return "";
        }
        
        try {
            return ReferenceConstants.RFT_NOTE_TAG + URLEncoder.encode(note, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }

}
