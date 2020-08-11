package edu.asu.diging.vspace.core.data.display;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.display.impl.SpaceTextBlockDisplay;

@Repository
@JaversSpringDataAuditable
public interface SpaceTextBlockDisplayRepository extends PagingAndSortingRepository<SpaceTextBlockDisplay, String> {

}
