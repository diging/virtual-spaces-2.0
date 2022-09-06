package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
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
       
    @Query("SELECT DISTINCT module from Module module JOIN ModuleLink moduleLink ON module.id = moduleLink.module.id JOIN Space space on space.id = moduleLink.space.id where module.moduleStatus =?1 AND space.spaceStatus = ?2 AND ( module.name like %?3% OR module.description like %?4% )") 
    Page<IModule> findDistinctByModuleStatusNameContainingOrDescriptionContainingLinkedToSpace(Pageable requestedPage, ModuleStatus moduleStatus, SpaceStatus spaceStatus, String name,
           String description);
}
