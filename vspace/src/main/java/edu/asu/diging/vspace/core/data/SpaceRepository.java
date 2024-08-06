package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

@Repository
@JaversSpringDataAuditable
public interface SpaceRepository extends PagingAndSortingRepository<Space, String> {

    List<Space> findTop5ByOrderByCreationDateDesc();

    List<Space> findAllBySpaceStatus(SpaceStatus spaceStatus);

    List<Space> findAllByImageId(String imageId);

    Page<ISpace> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);
    
    @Query("SELECT distinct l FROM Space s JOIN s.spaceNames l WHERE l.exhibitionLanguage=?2 AND s=?1")
    LocalizedText findNamesBySpaceAndExhibitionLanguage(Space space, ExhibitionLanguage language);
    
    @Query("SELECT distinct l FROM Space s JOIN s.spaceDescriptions l WHERE l.exhibitionLanguage=?2 AND s=?1 ORDER BY l.id desc")
    LocalizedText findDescriptionsBySpaceAndExhibitionLanguage(Space space, ExhibitionLanguage language);
}