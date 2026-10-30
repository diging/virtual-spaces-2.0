package edu.asu.diging.vspace.web.api;

import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

/**
 * Builds response headers for serving uploaded media. Uploaded files are
 * user controlled, so the browser must never treat them as a web page of
 * this application:
 * <ul>
 * <li>{@code Content-Security-Policy: sandbox} stops scripts in a file
 * (e.g. an SVG or HTML file) from running when the URL is opened directly.
 * It has no effect when the file is shown in an {@code <img>} or
 * {@code <video>} tag.</li>
 * <li>{@code X-Content-Type-Options: nosniff} stops the browser from
 * guessing a different content type.</li>
 * <li>Anything that is not an image, video or audio file is sent as a
 * download instead of being rendered.</li>
 * </ul>
 */
public final class MediaResponseHeaders {

    public static final String CONTENT_SECURITY_POLICY = "sandbox";

    private MediaResponseHeaders() {
    }

    public static HttpHeaders create(String fileType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.set("Content-Security-Policy", CONTENT_SECURITY_POLICY);
        headers.set("X-Content-Type-Options", "nosniff");

        MediaType mediaType = parseMediaType(fileType);
        if (mediaType != null && isInlineType(mediaType)) {
            headers.setContentType(mediaType);
        } else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().build());
        }
        return headers;
    }

    private static boolean isInlineType(MediaType mediaType) {
        String type = mediaType.getType();
        return "image".equals(type) || "video".equals(type) || "audio".equals(type);
    }

    private static MediaType parseMediaType(String fileType) {
        if (fileType == null) {
            return null;
        }
        try {
            return MediaType.parseMediaType(fileType);
        } catch (InvalidMediaTypeException e) {
            return null;
        }
    }
}
