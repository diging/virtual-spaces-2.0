package edu.asu.diging.vspace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;

@Component
@Configuration
public class StorageEngineConfig {
    
    @Value("${downloads_path}")
    private String downloadsPath;
    
    @Value("${uploads_path}")
    private String uploadsPath;
    
    @Bean(name = "storageEngineDownloads")
    public IStorageEngine storageEngineDownloads(){
        return new StorageEngine(downloadsPath);
    }
    
    @Bean(name = "storageEngineUploads")
    public IStorageEngine storageEngineUploads(){
        return new StorageEngine(uploadsPath); 
    }

}
