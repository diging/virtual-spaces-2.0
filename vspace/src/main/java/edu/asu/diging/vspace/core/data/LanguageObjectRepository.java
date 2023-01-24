package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;


@Repository
@JaversSpringDataAuditable
public interface LanguageObjectRepository extends PagingAndSortingRepository<LanguageDescriptionObject, String>{

}
