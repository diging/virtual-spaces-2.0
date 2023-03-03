package edu.asu.diging.vspace.core.references.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.hibernate.sql.Template;
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
@PropertySource({ "classpath:config_reference.properties" })
public class CoinSMetadataProvider implements IReferenceMetadataProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Environment env;

    @Override
    public ReferenceMetadataType getReferenceMetadataType() {
        return ReferenceMetadataType.DEFAULT;
    }

    public static void dataSetup(HashMap<String, String> referenceMap) {

        referenceMap.put(CoinSConstants.BOOK_GENRE, CoinSConstants.BOOK_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.BOOK_SECTION_GENRE, CoinSConstants.BOOK_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.CONFERENCE_PAPER, CoinSConstants.BOOK_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.JOURNAL_ARTICLE_GENRE, CoinSConstants.ARTICLE_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.BLOGPOST_TYPE, CoinSConstants.BLOGPOST_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.ART_TYPE, CoinSConstants.BLOGPOST_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.PATENT_TYPE, CoinSConstants.PATENT_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.PODCAST, CoinSConstants.BLOGPOST_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.PRESENTATION, CoinSConstants.BLOGPOST_RFT_VAL_FMT);
        referenceMap.put(CoinSConstants.SOFTWARE_DOCUMENTATION, CoinSConstants.BLOGPOST_RFT_VAL_FMT);

    }

    public static String getReferenceData(String referenceType) {
        HashMap<String, String> referenceMap = new HashMap<>();
        dataSetup(referenceMap);
        return referenceMap.getOrDefault(referenceType, CoinSConstants.DEFAULT_RFT_VAL_FMT);

    }

    @Override
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException {

        String urlEncodedReferenceMetaData = CoinSConstants.DEFAULT_URL_VERSION + "&"
                + CoinSConstants.DEFAULT_CTX_VERSION;
        StringBuffer urlEncodedReferenceMetaDataStringBuffer = new StringBuffer(urlEncodedReferenceMetaData);
        try {
            urlEncodedReferenceMetaData += CoinSConstants.RFR_ID_TAG
                    + URLEncoder.encode(CoinSConstants.DEFAULT_RFR_ID, StandardCharsets.UTF_8.name());
            urlEncodedReferenceMetaData += CoinSConstants.RFT_VAL_FMT_TAG;
            String referenceTypeRFTValueString = getReferenceData(reference.getType());
            urlEncodedReferenceMetaData += URLEncoder.encode(referenceTypeRFTValueString);
            urlEncodedReferenceMetaData += getRefTitleEncoded(reference.getTitle(), reference.getType())
                    + getRefAuthorEncoded(reference.getAuthor()) + getRefYearEncoded(reference.getYear())
                    + getRefJournalEncoded(reference.getJournal()) + getRefUrlEncoded(reference.getUrl())
                    + getRefVolumeEncoded(reference.getVolume()) + getRefIssueEncoded(reference.getIssue())
                    + getRefPagesEncoded(reference.getPages()) + getRefEditorsEncoded(reference.getEditors())
                    + getRefNoteEncoded(reference.getNote()) + getRefTypeEncoded(reference.getType());
            urlEncodedReferenceMetaDataStringBuffer.append(CoinSConstants.RFR_ID_TAG);
            urlEncodedReferenceMetaDataStringBuffer
                    .append(URLEncoder.encode(CoinSConstants.DEFAULT_RFR_ID, StandardCharsets.UTF_8.name()));
            urlEncodedReferenceMetaDataStringBuffer.append(URLEncoder.encode(referenceTypeRFTValueString));
            urlEncodedReferenceMetaDataStringBuffer.append(getRefTitleEncoded(reference.getTitle(), reference.getType())
                    + getRefAuthorEncoded(reference.getAuthor()) + getRefYearEncoded(reference.getYear())
                    + getRefJournalEncoded(reference.getJournal()) + getRefUrlEncoded(reference.getUrl())
                    + getRefVolumeEncoded(reference.getVolume()) + getRefIssueEncoded(reference.getIssue())
                    + getRefPagesEncoded(reference.getPages()) + getRefEditorsEncoded(reference.getEditors())
                    + getRefNoteEncoded(reference.getNote()) + getRefTypeEncoded(reference.getType()));
            System.out.println("StringBuffer : " + urlEncodedReferenceMetaDataStringBuffer);
            System.out.println("String : " + urlEncodedReferenceMetaData);
            

        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
        urlEncodedReferenceMetaData += CoinSConstants.RFT_DEFAULT_LANGUAGE;
        return urlEncodedReferenceMetaData;
    }

    private String getRefTypeEncoded(String type) throws ReferenceMetadataEncodingException {
        if (type == null || type.trim().equals("")) {
            return "";
        }

        try {

            if (type.equals(CoinSConstants.BLOGPOST_TYPE) || type.equals(CoinSConstants.PODCAST)
                    || type.equals(CoinSConstants.PRESENTATION) || type.equals(CoinSConstants.SOFTWARE_DOCUMENTATION)) {
                return CoinSConstants.RFT_TYPE_TAG + URLEncoder.encode(type, StandardCharsets.UTF_8.name());

            }
            return CoinSConstants.RFT_GENRE_TAG + URLEncoder.encode(type, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefTitleEncoded(String title, String type) throws ReferenceMetadataEncodingException {
        if (title == null || title.trim().equals("")) {
            return "";
        }

        try {
            if (type.equals(CoinSConstants.BOOK_GENRE)) {
                System.out.println(type + CoinSConstants.RFT_BOOKTITLE_TAG);
                return CoinSConstants.RFT_BOOKTITLE_TAG + URLEncoder.encode(title, StandardCharsets.UTF_8.name());
            } else if (type.equals(CoinSConstants.JOURNAL_ARTICLE_GENRE)
                    || type.equals(CoinSConstants.BOOK_SECTION_GENRE)) {
                return CoinSConstants.RFT_ARTICLETITLE_TAG + URLEncoder.encode(title, StandardCharsets.UTF_8.name());
            }
            return CoinSConstants.RFT_TITLE_TAG + URLEncoder.encode(title, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefAuthorEncoded(String author) throws ReferenceMetadataEncodingException {
        if (author == null || author.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_AUTHOR_TAG + URLEncoder.encode(author, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefLastAuthorEncoded(String author) throws ReferenceMetadataEncodingException {
        if (author == null || author.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_LASTAUTHOR_TAG + URLEncoder.encode(author, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefYearEncoded(String year) throws ReferenceMetadataEncodingException {
        if (year == null || year.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_DATE_TAG + URLEncoder.encode(year, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefJournalEncoded(String journal) throws ReferenceMetadataEncodingException {
        if (journal == null || journal.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_JOURNAL_TAG + URLEncoder.encode(journal, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefUrlEncoded(String url) throws ReferenceMetadataEncodingException {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_ID_TAG + URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefVolumeEncoded(String volume) throws ReferenceMetadataEncodingException {
        if (volume == null || volume.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_VOLUME_TAG + URLEncoder.encode(volume, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefIssueEncoded(String issue) throws ReferenceMetadataEncodingException {
        if (issue == null || issue.trim().equals("")) {
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
        if (pages != null && !pages.trim().equals("")) {
            try {
                refPagesEncoded += CoinSConstants.RFT_ISSUE_TAG
                        + URLEncoder.encode(pages, StandardCharsets.UTF_8.name());
                String[] tokens = pages.split("-");
                if (tokens.length >= 2) {
                    tokens = pages.split("-", 2);
                }
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
        if (editors == null || editors.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_EDITOR_TAG + URLEncoder.encode(editors, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }

    private String getRefNoteEncoded(String note) throws ReferenceMetadataEncodingException {
        if (note == null || note.trim().equals("")) {
            return "";
        }
        try {
            return CoinSConstants.RFT_NOTE_TAG + URLEncoder.encode(note, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new ReferenceMetadataEncodingException(e);
        }
    }
}