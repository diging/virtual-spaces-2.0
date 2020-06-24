package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.ModuleLink;

@Repository
@JaversSpringDataAuditable
public interface ModuleLinkRepository extends PagingAndSortingRepository<ModuleLink, String> {

}
