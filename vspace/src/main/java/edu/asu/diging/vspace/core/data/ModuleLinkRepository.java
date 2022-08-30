package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.IModuleLink;

@Repository
@JaversSpringDataAuditable
public interface ModuleLinkRepository extends PagingAndSortingRepository<ModuleLink, String> {

	
    List<ModuleLink> findByModuleId(IModule module);
       
    @Query("select m from ModuleLink m where m.module.id = ?1")
    List<ModuleLink> getModuleLinks(String moduleId);
    
    @Query("select s.space.id from ModuleLink s where s.id = ?1")
    String getSpaceIdFromModuleLink(String link);    

    
    @Query("select moduleLink from ModuleLink moduleLink where module_id = ?1")
    List<IModuleLink> findModuleLinksByModuleId(String moduleId);

}
