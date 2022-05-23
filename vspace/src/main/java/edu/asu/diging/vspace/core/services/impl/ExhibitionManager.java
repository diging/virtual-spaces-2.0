package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Transactional
@Service
public class ExhibitionManager implements IExhibitionManager {

    @Autowired
    private ExhibitionRepository exhibitRepo;
    
    @Autowired
    private ExhibitionLanguageConfig exhibitionLanguageConfig;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());


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
     */
    @Override
    public void updateExhibitionLanguages(Exhibition exhibition, List<String> codes, String defaultLanguage) {
        List<ExhibitionLanguage> languageMapList = new ArrayList();
        if(CollectionUtils.isNotEmpty(codes)) {
            if(defaultLanguage!=null) {
                codes.add(defaultLanguage);
            }
            codes.forEach(code -> {
                Optional<ExhibitionLanguage> languageMap = exhibitionLanguageConfig.getExhibitionLanguageList()
                        .stream().filter(map-> code.equalsIgnoreCase((String) map.get("code")))
                        .map(language -> { 
                            ExhibitionLanguage exhibitionLanguage =   new ExhibitionLanguage((String) language.get("label"),
                                    (String) language.get("code"), exhibition);

                            if(exhibitionLanguage.getCode().equalsIgnoreCase(defaultLanguage)) {
                                exhibitionLanguage.setDefault(true);
                            }
                            return exhibitionLanguage;

                        }).findFirst();

                if(languageMap.isPresent()) {

                    languageMapList.add(languageMap.get());
                }
            });


            if(CollectionUtils.isNotEmpty(languageMapList)) {
                logger.info("Updating Exhibition with Languages" + codes);
                exhibition.getLanguages().addAll(languageMapList);
            }
        }

    }



}
