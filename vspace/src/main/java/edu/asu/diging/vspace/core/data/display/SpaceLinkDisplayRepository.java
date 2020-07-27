package edu.asu.diging.vspace.core.data.display;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

@Repository
@JaversSpringDataAuditable
public interface SpaceLinkDisplayRepository extends PagingAndSortingRepository<SpaceLinkDisplay, String> {

    @Query("SELECT d FROM SpaceLinkDisplay d WHERE d.link.sourceSpace.id = ?1")
    public List<SpaceLinkDisplay> findSpaceLinkDisplaysForSpace(String spaceId);

    @Query("SELECT d FROM SpaceLinkDisplay d WHERE d.link.sourceSpace.id = ?1 AND d.link.targetSpace.spaceStatus = ?2")
    public List<SpaceLinkDisplay> findSpaceLinkDisplaysToPublishedSpace(String spaceId, SpaceStatus spaceStatus);

    @Modifying
    @Query("delete from SpaceLinkDisplay d where d.link.id in (:linkIds)")
    void deleteByLinkId(@Param("linkIds") List<String> linkId);

    @Modifying
    @Query("delete from SpaceLinkDisplay d where d.link.id = ?1")
    void deleteBySpaceLinkId(String spaceLinkId);

    public void deleteByLink(ISpaceLink space);

}
