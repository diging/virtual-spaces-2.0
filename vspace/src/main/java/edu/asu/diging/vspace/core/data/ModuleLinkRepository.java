package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.IModuleLink;

@Repository
@JaversSpringDataAuditable
public interface ModuleLinkRepository extends PagingAndSortingRepository<ModuleLink, String> {

    @Query("select moduleLink from ModuleLink moduleLink where module_id = ?1")
    List<IModuleLink> findModuleLinksByModuleId(String moduleId);

}
