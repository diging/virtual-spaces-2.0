package edu.asu.diging.vspace.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;

@Component
@Configuration
public class StorageEngineConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * The paths come from app.properties (set in pom.xml) and can be overridden
     * with the environment variables VSPACE_DOWNLOADS_PATH and VSPACE_UPLOADS_PATH.
     */
    
    @Value("${VSPACE_DOWNLOADS_PATH:${downloads_path}}")
    private String downloadsPath;
    
    @Value("${VSPACE_UPLOADS_PATH:${uploads_path}}")
    private String uploadsPath;
    
    @Bean(name = "storageEngineDownloads")
    public IStorageEngine storageEngineDownloads(){
        logger.info("Storing exhibition snapshots in " + new File(downloadsPath).getAbsolutePath());
        return new StorageEngine(downloadsPath);
    }
    
    @Bean(name = "storageEngineUploads")
    public IStorageEngine storageEngineUploads(){
        return new StorageEngine(uploadsPath); 
    }
}
