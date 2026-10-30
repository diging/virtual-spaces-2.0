package edu.asu.diging.vspace.web.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class MediaResponseHeadersTest {

    @Test
    public void test_create_image() {
        HttpHeaders headers = MediaResponseHeaders.create("image/png");
        assertEquals(MediaType.IMAGE_PNG, headers.getContentType());
        assertNull(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertSecurityHeaders(headers);
    }

    @Test
    public void test_create_svgStaysInline() {
        HttpHeaders headers = MediaResponseHeaders.create("image/svg+xml");
        assertEquals(MediaType.parseMediaType("image/svg+xml"), headers.getContentType());
        assertNull(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertSecurityHeaders(headers);
    }

    @Test
    public void test_create_video() {
        HttpHeaders headers = MediaResponseHeaders.create("video/mp4");
        assertEquals(MediaType.parseMediaType("video/mp4"), headers.getContentType());
        assertSecurityHeaders(headers);
    }

    @Test
    public void test_create_htmlIsDownloaded() {
        HttpHeaders headers = MediaResponseHeaders.create("text/html");
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());
        assertEquals("attachment", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertSecurityHeaders(headers);
    }

    @Test
    public void test_create_invalidOrMissingType() {
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, MediaResponseHeaders.create("not a type").getContentType());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, MediaResponseHeaders.create(null).getContentType());
    }

    private void assertSecurityHeaders(HttpHeaders headers) {
        assertEquals("sandbox", headers.getFirst("Content-Security-Policy"));
        assertEquals("nosniff", headers.getFirst("X-Content-Type-Options"));
    }
}
