package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;

@Repository
@JaversSpringDataAuditable
public interface ExhibitionLanguageRepository extends PagingAndSortingRepository<ExhibitionLanguage, String>{

    ExhibitionLanguage findByLabel(String label);
    
    @Query("SELECT l FROM ExhibitionLanguage l WHERE l.exhibition = ?1 AND l.isDefault = true")
    IExhibitionLanguage findByExhibitionAndIsDefault(IExhibition exhibition);
    
    IExhibitionLanguage findByExhibitionAndCode(IExhibition exhibition, String code);
    
}

