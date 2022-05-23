package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
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

    /**
     * Updates the Exhibition with given list of languages. It fetches the language from exhibitionLanguageConfig using code.
     *  
     * @param exhibition
     * @param languages
     */
    public void updateExhibitionLanguages(Exhibition exhibition, List<String> codes) {
        List<ExhibitionLanguage> languageMapList = new ArrayList();
        codes.forEach(code -> {
            Optional<ExhibitionLanguage> languageMap = exhibitionLanguageConfig.getExhibitionLanguageList()
                    .stream().filter(map-> code.equalsIgnoreCase((String) map.get("code")))
                    .map(exhibitLanguage -> new ExhibitionLanguage((String) exhibitLanguage.get("label"),
                            (String) exhibitLanguage.get("code"), exhibition)).findFirst();

            if(languageMap.isPresent()) {
                languageMapList.add(languageMap.get());
            }
        });
        if(CollectionUtils.isNotEmpty(languageMapList)) {
            exhibition.getLanguages().addAll(languageMapList);
        }


    }

}
