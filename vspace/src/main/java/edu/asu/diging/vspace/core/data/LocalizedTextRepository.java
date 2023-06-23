package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;

@Repository
@JaversSpringDataAuditable
public interface LocalizedTextRepository extends PagingAndSortingRepository<LocalizedText, String>{
    
    List<LocalizedText> findByExhibitionLanguage(IExhibitionLanguage language);

}
