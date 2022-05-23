package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.factory.IExhibitionFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;

@Service
public class ExhibitionFactory implements IExhibitionFactory {
    
    @Autowired
    private ExhibitionLanguageConfig exhibitionLanguageConfig;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.Exhibition
     */
    @Override
    public IExhibition createExhibition() {
        return new Exhibition();
    }

    public void updateExhibitionLanguages(Exhibition exhibition, List<String> languages) {
        languages.forEach(language -> {
            
           List<ExhibitionLanguage> languageMapList = exhibitionLanguageConfig.getExhibitionLanguageList().stream().filter(languageMap -> 
            languages.contains((String)languageMap.get("label"))
            ).map(map -> new ExhibitionLanguage((String) map.get("label"), (String) map.get("code"), exhibition)).collect(Collectors.toList());
           
          exhibition.getLanguages().addAll(languageMapList);
        });
        
    }

}
