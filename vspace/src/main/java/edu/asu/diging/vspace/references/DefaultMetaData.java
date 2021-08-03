package edu.asu.diging.vspace.references;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.impl.Reference;

@Service
public class DefaultMetaData implements ReferenceMetadataProvider {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //These all values are fixed for default metadata 
    private String urlVersion = "url_ver=Z39.88-2004";
    
    private String ctxVersion = "ctx_ver=Z39.88-2004";
    
    private String rfrId = "info:sid/zotero.org:2";
    
    private String rftValFmt = "info:ofi/fmt:kev:mtx:dissertation";

    @Override
    public ReferenceMetaDataType getReferenceMetadataType() {
        return ReferenceMetaDataType.DEFAULT;
    }

    @Override
    public String getReferenceMetadata(Reference reference) {
        String urlEncodedReferenceMetaData = urlVersion + "&" + ctxVersion;
        try {
            urlEncodedReferenceMetaData += "&rfr_id=" + URLEncoder.encode(rfrId, StandardCharsets.UTF_8.toString());
            urlEncodedReferenceMetaData += "&rft_val_fmt="
                    + URLEncoder.encode(rftValFmt, StandardCharsets.UTF_8.toString());
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
                urlEncodedReferenceMetaData += ("&rft.degree=")
                        + URLEncoder.encode(type, StandardCharsets.UTF_8.toString());

            if (title != null)
                urlEncodedReferenceMetaData += ("&rft.title=")
                        + URLEncoder.encode(title, StandardCharsets.UTF_8.toString());

            if (author != null)
                urlEncodedReferenceMetaData += ("&rft.au=") + URLEncoder.encode(author, StandardCharsets.UTF_8.toString());

            if (year != null)
                urlEncodedReferenceMetaData += ("&rft.date=") + URLEncoder.encode(year, StandardCharsets.UTF_8.toString());

            if (journal != null)
                urlEncodedReferenceMetaData += ("&rft.jtitle=")
                        + URLEncoder.encode(journal, StandardCharsets.UTF_8.toString());

            if (url != null)
                urlEncodedReferenceMetaData += ("&rft_id=") + URLEncoder.encode(url, StandardCharsets.UTF_8.toString());

            if (volume != null)
                urlEncodedReferenceMetaData += ("&rft.volume=")
                        + URLEncoder.encode(volume, StandardCharsets.UTF_8.toString());

            if (issue != null)
                urlEncodedReferenceMetaData += ("&rft.issue=")
                        + URLEncoder.encode(issue, StandardCharsets.UTF_8.toString());

            if (pages != null) {
                urlEncodedReferenceMetaData += ("&rft.pages=")
                        + URLEncoder.encode(pages, StandardCharsets.UTF_8.toString());
                String[] tokens = pages.split("-", 2);
                if (tokens.length > 1) {
                    urlEncodedReferenceMetaData += ("&rft.spage=")
                            + URLEncoder.encode(tokens[0], StandardCharsets.UTF_8.toString());
                    urlEncodedReferenceMetaData += ("&rft.epage=")
                            + URLEncoder.encode(tokens[1], StandardCharsets.UTF_8.toString());
                }
            }

            if (editors != null)
                urlEncodedReferenceMetaData += ("&rft.editors=")
                        + URLEncoder.encode(editors, StandardCharsets.UTF_8.toString());

            if (note != null)
                urlEncodedReferenceMetaData += ("&rft.note=") + URLEncoder.encode(note, StandardCharsets.UTF_8.toString());

            urlEncodedReferenceMetaData += ("&rft.language=en");

        } catch (UnsupportedEncodingException e) {
            logger.error("Error while encoding reference metadata. ", e);
        }

        return urlEncodedReferenceMetaData;
    }

}
