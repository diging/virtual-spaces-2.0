package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.Module;

@Repository
@JaversSpringDataAuditable
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

    List<Module> findTop5ByOrderByCreationDateDesc();

    Page<IModule> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);

    List<Module> findAllByOrderByCreationDateDesc();
    
    List<IModule> findAllByStartSequenceNotNull();
} 
