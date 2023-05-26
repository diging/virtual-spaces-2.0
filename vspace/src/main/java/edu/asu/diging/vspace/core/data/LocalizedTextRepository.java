package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;


@Repository
@JaversSpringDataAuditable
public interface LocalizedTextRepository extends PagingAndSortingRepository<LocalizedText, String>{

}
