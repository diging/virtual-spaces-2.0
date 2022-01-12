package edu.asu.diging.vspace.core.references.impl;

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
import edu.asu.diging.vspace.core.references.CoinSConstants;
import edu.asu.diging.vspace.core.references.IReferenceMetadataProvider;
import edu.asu.diging.vspace.core.references.ReferenceMetadataType;

@Service
@PropertySource({"classpath:config_reference.properties"})
public class CoinSMetadataProvider implements IReferenceMetadataProvider {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private Environment env;
    
    @Override
    public ReferenceMetadataType getReferenceMetadataType() {
        return ReferenceMetadataType.DEFAULT;
    }

    @Override
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException {
        String urlEncodedReferenceMetaData = CoinSConstants.DEFAULT_URL_VERSION + "&" + CoinSConstants.DEFAULT_CTX_VERSION;
        try {
            urlEncodedReferenceMetaData += CoinSConstants.RFR_ID_TAG + URLEncoder.encode(CoinSConstants.DEFAULT_RFR_ID, StandardCharsets.UTF_8.name());
            urlEncodedReferenceMetaData += CoinSConstants.RFT_VAL_FMT_TAG
                    + URLEncoder.encode(CoinSConstants.DEFAULT_RFT_VAL_FMT, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }

        urlEncodedReferenceMetaData += getRefTypeEncoded(env.getProperty(reference.getType())) + getRefTitleEncoded(reference.getTitle()) + getRefAuthorEncoded(reference.getAuthor()) + getRefYearEncoded(reference.getYear())
            + getRefJournalEncoded(reference.getJournal()) + getRefUrlEncoded(reference.getUrl()) + getRefVolumeEncoded(reference.getVolume()) + getRefIssueEncoded(reference.getIssue()) + getRefPagesEncoded(reference.getPages())
            + getRefEditorsEncoded(reference.getEditors()) + getRefNoteEncoded(reference.getNote());

        urlEncodedReferenceMetaData += CoinSConstants.RFT_DEFAULT_LANGUAGE;

        return urlEncodedReferenceMetaData;
    }
    
    private String getRefTypeEncoded(String type) throws ReferenceMetadataEncodingException {
        
        if(type==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_DEGREE_TAG + URLEncoder.encode(type, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefTitleEncoded(String title) throws ReferenceMetadataEncodingException {
        
        if(title==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_TITLE_TAG + URLEncoder.encode(title, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefAuthorEncoded(String author) throws ReferenceMetadataEncodingException {
        
        if(author==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_AUTHOR_TAG + URLEncoder.encode(author, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefYearEncoded(String year) throws ReferenceMetadataEncodingException {
        
        if(year==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_DATE_TAG + URLEncoder.encode(year, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefJournalEncoded(String journal) throws ReferenceMetadataEncodingException {
        
        if(journal==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_JOURNAL_TAG + URLEncoder.encode(journal, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefUrlEncoded(String url) throws ReferenceMetadataEncodingException {
        
        if(url==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_ID_TAG + URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefVolumeEncoded(String volume) throws ReferenceMetadataEncodingException {
        
        if(volume==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_VOLUME_TAG + URLEncoder.encode(volume, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefIssueEncoded(String issue) throws ReferenceMetadataEncodingException {
        
        if(issue==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_ISSUE_TAG + URLEncoder.encode(issue, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefPagesEncoded(String pages) throws ReferenceMetadataEncodingException {
        String refPagesEncoded = "";
        if (pages != null) {
            try {
                refPagesEncoded += CoinSConstants.RFT_ISSUE_TAG
                        + URLEncoder.encode(pages, StandardCharsets.UTF_8.name());
                String[] tokens = pages.split("-", 2);
                if (tokens.length > 1) {
                    refPagesEncoded += CoinSConstants.RFT_START_PAGE_TAG
                            + URLEncoder.encode(tokens[0], StandardCharsets.UTF_8.name());
                    refPagesEncoded += CoinSConstants.RFT_END_PAGE_TAG
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
            return CoinSConstants.RFT_EDITOR_TAG + URLEncoder.encode(editors, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }
    
    private String getRefNoteEncoded(String note) throws ReferenceMetadataEncodingException {
        
        if(note==null) {
            return "";
        }
        
        try {
            return CoinSConstants.RFT_NOTE_TAG + URLEncoder.encode(note, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }  
    }

}
