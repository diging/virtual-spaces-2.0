package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Exhibition;

@Repository
@JaversSpringDataAuditable
public interface ExhibitionRepository extends PagingAndSortingRepository<Exhibition, String> {

}
