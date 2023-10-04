package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;

@Service
public class FileFactory implements IFileFactory {
    
    @Override
    public IVSFile createFile(String filename, String filetype) {
        IVSFile file = new VSFile();
        file.setFilename(filename);
        file.setFileType(filetype);
        return file;
    }
}
