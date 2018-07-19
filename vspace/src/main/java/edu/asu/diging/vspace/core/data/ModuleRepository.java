package edu.asu.diging.vspace.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Module;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

}
