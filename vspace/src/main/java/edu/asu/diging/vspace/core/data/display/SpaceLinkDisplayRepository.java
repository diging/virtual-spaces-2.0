package edu.asu.diging.vspace.core.data.display;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkDisplayRepository extends PagingAndSortingRepository<SpaceLinkDisplay, String> {

	@Query("SELECT d FROM SpaceLinkDisplay d WHERE d.link.sourceSpace.id = ?1")
	public List<SpaceLinkDisplay> findSpaceLinkDisplaysForSpace(String spaceId);
	
	@Modifying
    @Query("delete from SpaceLinkDisplay where id in (:linkIds)")
    void deleteByLinkId(@Param("linkIds") List<String> linkId);
}
