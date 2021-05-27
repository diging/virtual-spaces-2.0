package edu.asu.diging.vspace.core.data.display;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;

@Repository
@JaversSpringDataAuditable
public interface ModuleLinkDisplayRepository extends PagingAndSortingRepository<ModuleLinkDisplay, String> {

    @Query("SELECT d FROM ModuleLinkDisplay d WHERE d.link.space.id = ?1")
    public List<IModuleLinkDisplay> findModuleLinkDisplaysForSpace(String spaceId);

    public void deleteByLink(IModuleLink link);
    
    @Query("SELECT d FROM ModuleLinkDisplay d")
    public List<IModuleLinkDisplay> findModuleLinkDisplaysForAllSpace();
}
