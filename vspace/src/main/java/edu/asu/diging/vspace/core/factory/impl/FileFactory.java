package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.model.IVSFile;

@Service
public class FileFactory implements IFileFactory {
    
    
    
    public IVSFile createFile(String filename, String filetype) {
        IVSFile file = new IVSFile();
        return file;
    }
}
