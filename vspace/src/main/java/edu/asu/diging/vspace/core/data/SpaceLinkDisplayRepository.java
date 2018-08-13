package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkDisplayRepository extends PagingAndSortingRepository<SpaceLinkDisplay, String> {

}
