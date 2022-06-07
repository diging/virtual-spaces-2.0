package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.config.ConfigConstants;
import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.exception.LanguageListConfigurationNotFound;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Transactional
@Service
public class ExhibitionManager implements IExhibitionManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExhibitionRepository exhibitRepo;
    
    @Autowired
    private ExhibitionLanguageConfig exhibitionLanguageConfig;
    

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
     * asu.diging.vspace.core.model.impl.Exhibition)
     */
    @Override
    public IExhibition storeExhibition(Exhibition exhibition) {
        return exhibitRepo.save(exhibition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.IExhibitionManager#getExhibitionById(java
     * .lang.String)
     */
    @Override
    public IExhibition getExhibitionById(String id) {
        Optional<Exhibition> exhibition = exhibitRepo.findById(id);
        if (exhibition.isPresent()) {
            return exhibition.get();
        }
        return null;
    }

    @Override
    public List<IExhibition> findAll() {
        Iterable<Exhibition> exhibitions = exhibitRepo.findAll();
        List<IExhibition> results = new ArrayList<>();
        exhibitions.forEach(e -> results.add((IExhibition) e));
        return results;
    }

    @Override
    public IExhibition getStartExhibition() {
        // for now we just take the first one created, there shouldn't be more than one
        List<Exhibition> exhibitions = exhibitRepo.findAllByOrderByIdAsc();
        if (exhibitions.size() > 0) {
            return exhibitions.get(0);
        }
        return null;
    }

    /**
     * Updates the Exhibition with given list of languages. It fetches the language from exhibitionLanguageConfig using code.
     *  
     * @param exhibition
     * @param defaultLanguage 
     * @param languages
     * @throws LanguageListConfigurationNotFound 
     */
    @Override
    public void updateExhibitionLanguages(Exhibition exhibition, List<String> codes, String defaultLanguage) throws LanguageListConfigurationNotFound {
        if(CollectionUtils.isNotEmpty(codes)) {
            
            // Adds defaultLanguage to codes list if not already exists.
            if(defaultLanguage!=null && !codes.contains(defaultLanguage)) {
                codes.add(defaultLanguage);
            }


            if(CollectionUtils.isNotEmpty(exhibitionLanguageConfig.getExhibitionLanguageList())) {
                exhibitionLanguageConfig.getExhibitionLanguageList().stream()
                .filter(languageConfig -> codes.contains(languageConfig.get(ConfigConstants.CODE)))
                .forEach(languageMap -> {
                    ExhibitionLanguage exhibitionLanguage =   new ExhibitionLanguage((String) languageMap.get(ConfigConstants.LABEL),
                            (String) languageMap.get(ConfigConstants.CODE), exhibition);

                    int index =  exhibition.getLanguages().indexOf(exhibitionLanguage);
                    if( index < 0 ) {
                        exhibition.getLanguages().add(exhibitionLanguage);
                    } 
                    else {
                        exhibitionLanguage =   exhibition.getLanguages().get(index);

                    }
                    updateDefault(exhibitionLanguage, defaultLanguage);

                });  

            } else {
                throw new LanguageListConfigurationNotFound("Exhibition Language Configuration not found");
            }
        } 

    }

    /**
     * Updates isDefault to true if equal to defaultLanguage. Otherwise sets to false.
     * 
     * @param exhibitionLanguage
     * @param defaultLanguage
     */
    private void updateDefault(ExhibitionLanguage exhibitionLanguage, String defaultLanguage) {
       
        if(exhibitionLanguage.getCode().equalsIgnoreCase(defaultLanguage)) {
            exhibitionLanguage.setDefault(true);
        } else {
            exhibitionLanguage.setDefault(false);
        }

    }

}
