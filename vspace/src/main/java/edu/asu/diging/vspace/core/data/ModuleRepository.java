package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;

@Repository
@JaversSpringDataAuditable
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

    List<Module> findTop5ByOrderByCreationDateDesc();

    Page<IModule> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);

    List<Module> findAllByOrderByCreationDateDesc();
    
    List<Module> findAllByModuleStatus(ModuleStatus moduleStatus);
    
    List<ISequence> findAllByStartSequenceNotNull();
    
    
    @Query("SELECT DISTINCT module from Module module JOIN ModuleLink moduleLink ON module.id = moduleLink.module.id where module.name like %?1% OR module.description like %?2%") 
    Page<IModule> findDistinctByNameContainingOrDescriptionContainingLinkedToSpace(Pageable requestedPage, String name,
            String description);
}
