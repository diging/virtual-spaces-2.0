package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;

@Repository
@JaversSpringDataAuditable
public interface ModuleLinkRepository extends PagingAndSortingRepository<ModuleLink, String> {
	
	List<ModuleLink> findByModuleId(IModule module);
       
    @Query("select m from ModuleLink m where m.module.id = ?1")
    List<ModuleLink> getModuleLinks(String moduleId);
    
    @Query("select s.space.id from ModuleLink s where s.id = ?1")
    String getSpaceIdFromModuleLink(String link);    
}
