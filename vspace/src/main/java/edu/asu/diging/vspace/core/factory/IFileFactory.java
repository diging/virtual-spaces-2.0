package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IVSFile;

public interface IFileFactory {

    IVSFile createFile(String filename, String filetype);

}
