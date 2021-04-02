package edu.asu.diging.vspace.core.data.display;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceTextBlockDisplay;

@Repository
@JaversSpringDataAuditable
public interface SpaceTextBlockDisplayRepository extends PagingAndSortingRepository<SpaceTextBlockDisplay, String> {

    @Query("SELECT d FROM SpaceTextBlockDisplay d WHERE d.spaceTextBlock.space.id = ?1")
    public List<ISpaceTextBlockDisplay> findSpaceTextBlockDisplaysForSpace(String spaceId);
    
    public void deleteBySpaceTextBlock(ISpaceTextBlock spaceTextBlock);

}
