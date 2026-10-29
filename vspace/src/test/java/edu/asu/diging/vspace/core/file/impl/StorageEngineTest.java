package edu.asu.diging.vspace.core.file.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.exception.FileStorageException;

public class StorageEngineTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private StorageEngine storageEngine;

    private File uploadsFolder;

    private File outsideFile;

    private final byte[] content = "file content".getBytes();

    @Before
    public void init() throws IOException {
        uploadsFolder = tempFolder.newFolder("uploads");
        outsideFile = tempFolder.newFile("secret.txt");
        Files.write(outsideFile.toPath(), "secret".getBytes());
        storageEngine = new StorageEngine();
        ReflectionTestUtils.setField(storageEngine, "path", uploadsFolder.getAbsolutePath());
    }

    @Test
    public void test_storeFile_success() throws Exception {
        storageEngine.storeFile(content, "stored", "FILE000000001");
        File stored = new File(uploadsFolder, "FILE000000001" + File.separator + "stored");
        assertArrayEquals(content, Files.readAllBytes(stored.toPath()));
    }

    @Test
    public void test_storeFile_traversalFileName() {
        assertThrows(FileStorageException.class,
                () -> storageEngine.storeFile(content, "../../evil.jsp", "FILE000000001"));
        assertThrows(FileStorageException.class,
                () -> storageEngine.storeFile(content, "..\\..\\evil.jsp", "FILE000000001"));
        assertThrows(FileStorageException.class,
                () -> storageEngine.storeFile(content, "..", "FILE000000001"));
        assertFalse(new File(tempFolder.getRoot(), "evil.jsp").exists());
    }

    @Test
    public void test_storeFile_traversalDirectory() {
        assertThrows(FileStorageException.class,
                () -> storageEngine.storeFile(content, "evil.jsp", ".."));
        assertThrows(FileStorageException.class,
                () -> storageEngine.storeFile(content, "evil.jsp", "../other"));
    }

    @Test
    public void test_downloadFile_traversal() {
        assertThrows(IOException.class,
                () -> storageEngine.downloadFile("../../secret.txt", "FILE000000001"));
        assertThrows(IOException.class,
                () -> storageEngine.getMediaContent("FILE000000001", "../../secret.txt"));
    }

    @Test
    public void test_renameFile_traversal() throws Exception {
        storageEngine.storeFile(content, "stored", "FILE000000001");
        assertFalse(storageEngine.renameFile("stored", "../../moved", "FILE000000001"));
        assertTrue(new File(uploadsFolder, "FILE000000001" + File.separator + "stored").exists());
    }

    @Test
    public void test_deleteFile_traversal() {
        assertFalse(storageEngine.deleteFile("../../secret.txt", "FILE000000001"));
        assertTrue(outsideFile.exists());
    }
}
