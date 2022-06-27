package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;

@Repository
@JaversSpringDataAuditable
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

    List<Module> findTop5ByOrderByCreationDateDesc();

    Page<IModule> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);

    List<Module> findAllByOrderByCreationDateDesc();
    
    List<Module> findAllByModuleStatus(ModuleStatus moduleStatus);
    
    List<ISequence> findAllByStartSequenceNotNull();
}
