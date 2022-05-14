package edu.asu.diging.vspace.core.data;

import java.util.Optional;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.IModuleLink;

@Repository
@JaversSpringDataAuditable
public interface ModuleLinkRepository extends PagingAndSortingRepository<ModuleLink, String> {
    Optional<ModuleLink> findByModule_Id(String moduleId);
    
    @Query("select moduleLink from ModuleLink moduleLink where module_id = ?1")
    List<IModuleLink> findModuleLinksByModuleId(String moduleId);

}
