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

@Component
@PropertySource(value= "classpath:exhibitionLanguages.properties" , factory=JsonPropertySourceFactory.class)
@Configuration
public class ExhibitionLanguageConfig {

    @Autowired
    private Environment environment;
    
    List<Map>  exhibitionLanguageList = new ArrayList();
    
    
    @PostConstruct
    public void init() {
        for(Iterator it = ((AbstractEnvironment) environment).getPropertySources().iterator(); it.hasNext(); ) {
            org.springframework.core.env.PropertySource propertySource = (org.springframework.core.env.PropertySource) it.next();
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource mapSource = ((MapPropertySource) propertySource);
                if("json-property".equals(mapSource.getName())) {
                    Map<String, Object> languageMap = mapSource.getSource();

                    exhibitionLanguageList = (List<Map>) languageMap.get("languages");

                }

            }
        }
    }
   
    public List<Map> getExhibitionLanguageList() {
        return exhibitionLanguageList;
    }
    public void setExhibitionLanguageList(List<Map> exhibitionLanguageList) {
        this.exhibitionLanguageList = exhibitionLanguageList;
    }
}
