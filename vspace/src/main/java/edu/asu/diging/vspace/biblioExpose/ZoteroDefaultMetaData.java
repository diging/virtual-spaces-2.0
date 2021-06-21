package edu.asu.diging.vspace.biblioExpose;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import edu.asu.diging.vspace.core.model.IBiblioBlock;

public class ZoteroDefaultMetaData implements IBiblioMetadata {

    String biblioMetadataStyle = "defaultZoteroMetadata";

    //For now its default
    ICitationStyle citationStyle;

    IBiblioBlock biblioBlock;
    
    //These all values are fixed for default metadata 
    String urlVersion = "url_ver=Z39.88-2004";
    
    String ctxVersion = "ctx_ver=Z39.88-2004";
    
    String rfrId = "info:sid/zotero.org:2";
    
    String rftValFmt = "info:ofi/fmt:kev:mtx:dissertation";
    
    ZoteroDefaultMetaData(ICitationStyle citationStyle, IBiblioBlock biblioBlock) {
        this.citationStyle = citationStyle;
        this.biblioBlock = biblioBlock;
    }

    @Override
    public String getBiblioMetadataStyle() {
        return biblioMetadataStyle;
    }

    @Override
    public String createMetadata(ICitationStyle citationStyle, IBiblioBlock biblioBlock) {
        String urlEncodedBiblioMetaData = urlVersion + "&" + ctxVersion;
        try {
            urlEncodedBiblioMetaData += "&rfr_id=" + URLEncoder.encode(rfrId, StandardCharsets.UTF_8.toString());
            urlEncodedBiblioMetaData += "&rft_val_fmt="
                    + URLEncoder.encode(rftValFmt, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String type = biblioBlock.getType();
        String title = biblioBlock.getTitle();
        String author = biblioBlock.getAuthor();
        String year = biblioBlock.getYear();
        String journal = biblioBlock.getJournal();
        String url = biblioBlock.getUrl();
        String volume = biblioBlock.getVolume();
        String issue = biblioBlock.getIssue();
        String pages = biblioBlock.getPages();
        String editors = biblioBlock.getEditors();
        String note = biblioBlock.getNote();

        try {
            if (type != null)
                urlEncodedBiblioMetaData += ("&rft.degree=")
                        + URLEncoder.encode(type, StandardCharsets.UTF_8.toString());

            if (title != null)
                urlEncodedBiblioMetaData += ("&rft.title=")
                        + URLEncoder.encode(title, StandardCharsets.UTF_8.toString());

            if (author != null)
                urlEncodedBiblioMetaData += ("&rft.au=") + URLEncoder.encode(author, StandardCharsets.UTF_8.toString());

            if (year != null)
                urlEncodedBiblioMetaData += ("&rft.date=") + URLEncoder.encode(year, StandardCharsets.UTF_8.toString());

            if (journal != null)
                urlEncodedBiblioMetaData += ("&rft.jtitle=")
                        + URLEncoder.encode(journal, StandardCharsets.UTF_8.toString());

            if (url != null)
                urlEncodedBiblioMetaData += ("&rft_id=") + URLEncoder.encode(url, StandardCharsets.UTF_8.toString());

            if (volume != null)
                urlEncodedBiblioMetaData += ("&rft.volume=")
                        + URLEncoder.encode(volume, StandardCharsets.UTF_8.toString());

            if (issue != null)
                urlEncodedBiblioMetaData += ("&rft.issue=")
                        + URLEncoder.encode(issue, StandardCharsets.UTF_8.toString());

            if (pages != null) {
                urlEncodedBiblioMetaData += ("&rft.pages=")
                        + URLEncoder.encode(pages, StandardCharsets.UTF_8.toString());
                String[] tokens = pages.split("-", 2);
                if (tokens.length > 1) {
                    urlEncodedBiblioMetaData += ("&rft.spage=")
                            + URLEncoder.encode(tokens[0], StandardCharsets.UTF_8.toString());
                    urlEncodedBiblioMetaData += ("&rft.epage=")
                            + URLEncoder.encode(tokens[1], StandardCharsets.UTF_8.toString());
                }
            }

            if (editors != null)
                urlEncodedBiblioMetaData += ("&rft.editors=")
                        + URLEncoder.encode(editors, StandardCharsets.UTF_8.toString());

            if (note != null)
                urlEncodedBiblioMetaData += ("&rft.note=") + URLEncoder.encode(note, StandardCharsets.UTF_8.toString());

            urlEncodedBiblioMetaData += ("&rft.language=en");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return urlEncodedBiblioMetaData;
    }

}
