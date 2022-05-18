package edu.asu.diging.vspace.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value= "classpath:exhibitionLanguages.json" , factory=JsonPropertySourceFactory.class)
@Configuration
public class ExhibitionLanguageConfigList {
    
    @Value("${languages}") 
    private List<Language> languages;
  
    
    public List<Language> getLanguages() {
        return languages;
    }


    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }


    private class Language {
        
        @Value("${code}") 
        private String code;
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getLabel() {
            return label;
        }
        public void setLabel(String label) {
            this.label = label;
        }
        
        @Value("${label}") 
        private String label;

    }
    
}
