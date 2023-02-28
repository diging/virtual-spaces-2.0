package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import edu.asu.diging.vspace.config.ConfigConstants;
import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.LanguageListConfigurationNotFoundException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

@Transactional
@Service
public class ExhibitionManager implements IExhibitionManager {

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private IImageService imageService;

    @Autowired
	private ImageRepository imageRepo;
	
	@Autowired
	private IStorageEngine storage;
	
	@Autowired
    private ExhibitionRepository exhibitRepo;
    
    @Autowired
    private ExhibitionLanguageConfig exhibitionLanguageConfig;
    
    @Autowired
    private ExhibitionFactory exhibitFactory;

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


    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
     * asu.diging.vspace.core.model.impl.Exhibition)
     */
    

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.IExhibitionManager#getExhibitionById(java
     * .lang.String)
     */
    

    /**
     * Updates the Exhibition with given list of languages. It fetches the language from exhibitionLanguageConfig using code.
     *  
     * @param exhibition
     * @param defaultLanguage 
     * @param languages
     * @throws LanguageListConfigurationNotFoundException 
     */
    @Override
    public void updateExhibitionLanguages(Exhibition exhibition, List<String> codes, String defaultLanguage) {
        if(CollectionUtils.isEmpty(exhibitionLanguageConfig.getExhibitionLanguageList())) {
            throw new LanguageListConfigurationNotFoundException("Exhibition Language Configuration not found");
        }

        if(CollectionUtils.isEmpty(codes) ) {
            return;
        }

        // Adds defaultLanguage to codes list if not already exists.
        if(!StringUtils.isEmpty(defaultLanguage) && !codes.contains(defaultLanguage)) {
            codes.add(defaultLanguage);
        }

        exhibitionLanguageConfig.getExhibitionLanguageList().stream()
            .filter(languageConfig -> codes.contains(languageConfig.get(ConfigConstants.CODE)))
            .forEach(languageMap -> {
                IExhibitionLanguage exhibitionLanguage =  addExhibitionLanguage(exhibition , languageMap);  
                exhibitionLanguage.setDefault(exhibitionLanguage.getCode().equalsIgnoreCase(defaultLanguage));
            });  

        // Removes exhibition langauge if unselected.
        exhibition.getLanguages().removeAll(exhibition.getLanguages().stream()
                .filter(language -> !codes.contains(language.getCode())).collect(Collectors.toList()));

    }

    /**
     * Adds exhibitionLanguage to exhibition if not already present. If already present, returns exhibitionLanguage from the exhibition.
     * 
     * @param exhibition
     * @param languageMap
     * @return
     */
    private IExhibitionLanguage addExhibitionLanguage(Exhibition exhibition, Map languageMap) {
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
    /**
     * Adds defaultImages to externallink, spaceLink and ModuleLink if not already present. 
     * 
     * @param image
     * @param filename
     * @param id
     * @return
     */
    @Override
    public IVSImage storeDefaultImage(byte[] image, String filename, String id) {
        
        IVSImage defaultImage = null;
        if (image != null && image.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(image);
            
            defaultImage = imageFactory.createDefaultImage(filename, contentType, id);
            defaultImage = imageRepo.save((VSImage) defaultImage);
            
        }

        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        
        if (defaultImage != null) {
            String relativePath = null;
            try {
                relativePath = storage.storeFile(image, filename, defaultImage.getId());
            } catch (FileStorageException e) {
                returnValue.getErrorMsgs().add("Default image could not be stored: " + e.getMessage());
            }
            defaultImage.setParentPath(relativePath);
            ImageData imageData = imageService.getImageData(image);
            if (imageData != null) {
                defaultImage.setHeight(imageData.getHeight());
                defaultImage.setWidth(imageData.getWidth());
            }
            imageRepo.save((VSImage) defaultImage);
            
        }
        return defaultImage;
    }


}
