package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import edu.asu.diging.vspace.config.ConfigConstants;
import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionLanguageDeletionException;
import edu.asu.diging.vspace.core.exception.LanguageListConfigurationNotFoundException;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Transactional
@Service
public class ExhibitionManager implements IExhibitionManager {

    @Autowired
    private ExhibitionRepository exhibitRepo;
    
    @Autowired
    private ExhibitionLanguageConfig exhibitionLanguageConfig;
    
    @Autowired
    private ExhibitionFactory exhibitFactory;
    
    @Autowired
    private LocalizedTextRepository localizedTextRepo;
    
    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
     * asu.diging.vspace.core.model.impl.Exhibition)
     */
    @Override
    public IExhibition storeExhibition(IExhibition exhibition) {
        return exhibitRepo.save((Exhibition) exhibition);
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
        exhibitions.forEach(e -> results.add(e));
        return results;
    }

    /**
     * Returns the start exhibition. If it does not exist, a new exhibition is created.
     */
    @Override
    public IExhibition getStartExhibition() {
        // for now we just take the first one created, there shouldn't be more than one
        List<Exhibition> exhibitions = exhibitRepo.findAllByOrderByIdAsc();
        IExhibition exhibition;
        if (exhibitions.size() > 0) {
            exhibition = exhibitions.get(0);
            String previewId = exhibition.getPreviewId();
            if(previewId==null || previewId.isEmpty()) {
                exhibitFactory.updatePreviewId(exhibition);
            }
        }
        else {
            exhibition = (Exhibition) exhibitFactory.createExhibition();
            storeExhibition(exhibition);
        }
        return exhibition;
    }

    /**
     * Updates the Exhibition with given list of languages. It fetches the language from exhibitionLanguageConfig using code.
     *  
     * @param exhibition
     * @param defaultLanguage 
     * @param languages
     * @throws ExhibitionLanguageDeletionException 
     */
    @Override
    public void updateExhibitionLanguages(Exhibition exhibition, List<String> codes, String defaultLanguage) throws ExhibitionLanguageDeletionException{
        if(CollectionUtils.isEmpty(exhibitionLanguageConfig.getExhibitionLanguageList())) {
            throw new LanguageListConfigurationNotFoundException("Exhibition Language Configuration not found");
        }

        if(CollectionUtils.isEmpty(codes) ) {
            return;
        }
        
        // Adds defaultLanguage to codes list if not already exists.
        if(StringUtils.hasText(defaultLanguage) && !codes.contains(defaultLanguage)) {
            codes.add(defaultLanguage);
        }

        exhibitionLanguageConfig.getExhibitionLanguageList().stream()
            .filter(languageConfig -> codes.contains(languageConfig.get(ConfigConstants.CODE)))
            .forEach(languageMap -> {
                IExhibitionLanguage exhibitionLanguage =  addExhibitionLanguage(exhibition , languageMap);  
                exhibitionLanguage.setDefault(exhibitionLanguage.getCode().equalsIgnoreCase(defaultLanguage));
            });

        // Removes exhibition language if unselected.
        List<IExhibitionLanguage> exhibitionLanguageToBeRemoved = exhibition.getLanguages().stream()
                .filter(language -> !codes.contains(language.getCode())).collect(Collectors.toList());
        
        exhibition.getLanguages().removeAll(exhibitionLanguageToBeRemoved);

        for (IExhibitionLanguage language  : exhibitionLanguageToBeRemoved ) {
            if(checkIfLocalizedTextsExists(language))  {
                throw new ExhibitionLanguageDeletionException("Exhibition cannot be deleted as it is not empty") ;
            }

        }       
        exhibition.getLanguages().removeAll(exhibitionLanguageToBeRemoved);
    }
    
    /**
     * Return true if given exhibition language has non empty localized texts linked to it
     * 
     */
    @Override
    public boolean checkIfLocalizedTextsExists(IExhibitionLanguage language)  {               

        //This returns true if non empty localized texts exist
        return !localizedTextRepo.findByExhibitionLanguage(language).isEmpty();
    }
    
    /**
     * Removes localized texts from parent entities and delete them.
     * 
     */
    @Override
    public void deleteLocalizedTexts(List<ILocalizedText> emptyLocalizedTexts) {
        
        List<LocalizedText> convertedLocalizedTextsList = emptyLocalizedTexts.stream()
                .map(localizedText -> (LocalizedText) localizedText)
                .collect(Collectors.toList());
        localizedTextRepo.deleteAll(convertedLocalizedTextsList);
    }

    /**
     * Adds exhibitionLanguage to exhibition if not already present. If already present, returns exhibitionLanguage from the exhibition.
     * 
     * @param exhibition
     * @param languageMap
     * @return
     */
    private IExhibitionLanguage addExhibitionLanguage(IExhibition exhibition, Map languageMap) {
        IExhibitionLanguage exhibitionLanguage =   new ExhibitionLanguage((String) languageMap.get(ConfigConstants.LABEL),
                (String) languageMap.get(ConfigConstants.CODE), exhibition);

        int index =  exhibition.getLanguages().indexOf(exhibitionLanguage);
        if( index < 0 ) {
            exhibition.getLanguages().add(exhibitionLanguage);
        } else {
            exhibitionLanguage = exhibition.getLanguages().get(index);
        }

        return exhibitionLanguage;
    }

    public IExhibitionLanguage getDefaultLanguage(IExhibition exhibition){
        return exhibitionLanguageRepository.findByExhibitionAndIsDefault(exhibition);
    }

}