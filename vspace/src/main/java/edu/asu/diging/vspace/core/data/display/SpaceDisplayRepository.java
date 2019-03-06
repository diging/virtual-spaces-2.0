package edu.asu.diging.vspace.core.data.display;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;

@Repository
@JaversSpringDataAuditable
public interface SpaceDisplayRepository extends PagingAndSortingRepository<SpaceDisplay, String> {

	public SpaceDisplay getBySpace(ISpace space);
}
