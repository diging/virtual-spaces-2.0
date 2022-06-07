package edu.asu.diging.vspace.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import org.springframework.core.env.MutablePropertySources;


@Component
@PropertySource(value= "classpath:exhibitionLanguages.properties" , factory=JsonPropertySourceFactory.class)
@Configuration
public class ExhibitionLanguageConfig {

    @Autowired
    private Environment environment;
    
    private List<Map>  exhibitionLanguageList = new ArrayList<Map>();
    
    /**
     * Fetches the configured language list from environment property source and stores in exhibitionLanguageList
     * 
     */
    @PostConstruct
    public void init() {
        
        org.springframework.core.env.PropertySource<?> propertySource =  ((AbstractEnvironment) environment).getPropertySources().get(ConfigConstants.EXHIBITION_LANGUAGE_LIST_PROPERTY);
        if( propertySource != null ) {
            Map<String, Object> languageMap = (Map<String, Object>) propertySource.getSource();
            exhibitionLanguageList = (List<Map>) languageMap.get(ConfigConstants.LANGUAGES);
        }
        
    }
   
    public List<Map> getExhibitionLanguageList() {
        return exhibitionLanguageList;
    }
    public void setExhibitionLanguageList(List<Map> exhibitionLanguageList) {
        this.exhibitionLanguageList = exhibitionLanguageList;
    }
}
