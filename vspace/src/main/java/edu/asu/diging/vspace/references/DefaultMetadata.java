package edu.asu.diging.vspace.references;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
public class DefaultMetadata implements ReferenceMetadataProvider {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public ReferenceMetaDataType getReferenceMetadataType() {
        return ReferenceMetaDataType.DEFAULT;
    }

    @Override
    public String getReferenceMetadata(Reference reference) {
        String urlEncodedReferenceMetaData = ReferenceConstants.defaultUrlVersion + "&" + ReferenceConstants.defaultCtxVersion;
        try {
            urlEncodedReferenceMetaData += ReferenceConstants.rfrIdTag + URLEncoder.encode(ReferenceConstants.defaultRfrId, StandardCharsets.UTF_8.toString());
            urlEncodedReferenceMetaData += ReferenceConstants.rftValFmtTag
                    + URLEncoder.encode(ReferenceConstants.defaultRftValFmt, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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

        try {
            if (type != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftDegreeTag)
                        + URLEncoder.encode(type, StandardCharsets.UTF_8.toString());

            if (title != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftTitleTag)
                        + URLEncoder.encode(title, StandardCharsets.UTF_8.toString());

            if (author != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftAuthorTag) + URLEncoder.encode(author, StandardCharsets.UTF_8.toString());

            if (year != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftDateTag) + URLEncoder.encode(year, StandardCharsets.UTF_8.toString());

            if (journal != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftJournalTag)
                        + URLEncoder.encode(journal, StandardCharsets.UTF_8.toString());

            if (url != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftIdTag) + URLEncoder.encode(url, StandardCharsets.UTF_8.toString());

            if (volume != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftVolumeTag)
                        + URLEncoder.encode(volume, StandardCharsets.UTF_8.toString());

            if (issue != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftIssueTag)
                        + URLEncoder.encode(issue, StandardCharsets.UTF_8.toString());

            if (pages != null) {
                urlEncodedReferenceMetaData += (ReferenceConstants.rftIssueTag)
                        + URLEncoder.encode(pages, StandardCharsets.UTF_8.toString());
                String[] tokens = pages.split("-", 2);
                if (tokens.length > 1) {
                    urlEncodedReferenceMetaData += (ReferenceConstants.rftStartPageTag)
                            + URLEncoder.encode(tokens[0], StandardCharsets.UTF_8.toString());
                    urlEncodedReferenceMetaData += (ReferenceConstants.rftEndPageTag)
                            + URLEncoder.encode(tokens[1], StandardCharsets.UTF_8.toString());
                }
            }

            if (editors != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftEditorTag)
                        + URLEncoder.encode(editors, StandardCharsets.UTF_8.toString());

            if (note != null)
                urlEncodedReferenceMetaData += (ReferenceConstants.rftNoteTag) + URLEncoder.encode(note, StandardCharsets.UTF_8.toString());

            urlEncodedReferenceMetaData += (ReferenceConstants.rftDefaultLanguage);

        } catch (UnsupportedEncodingException e) {
            logger.error("Error while encoding reference metadata. ", e);
        }

        return urlEncodedReferenceMetaData;
    }

}
