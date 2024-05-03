package edu.asu.diging.vspace.referenceExpose;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.PropertySource;

import edu.asu.diging.vspace.core.model.IReference;

@PropertySource("classpath:config.properties")
public class ZoteroDefaultMetaData implements IReferenceMetadata {

    String biblioMetadataStyle = "defaultZoteroMetadata";

    //For now its default
    ICitationStyle citationStyle;

    IReference reference;
    
    //These all values are fixed for default metadata 
    String urlVersion = "${urlVersion}";
    
    String ctxVersion = "${ctxVersion}";
    
    String rfrId = "${rfrId}";
    
    String rftValFmt = "${rftValFmt}";
    
    ZoteroDefaultMetaData(ICitationStyle citationStyle, IReference reference) {
        this.citationStyle = citationStyle;
        this.reference = reference;
    }

    @Override
    public String getReferenceMetadataStyle() {
        return biblioMetadataStyle;
    }

    @Override
    public String createReferenceMetadata(ICitationStyle citationStyle, IReference reference) {
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
            e.printStackTrace();
        }

        return urlEncodedReferenceMetaData;
    }

}
