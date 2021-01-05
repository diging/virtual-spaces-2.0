package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;

@Repository
@JaversSpringDataAuditable
public interface SpaceRepository extends PagingAndSortingRepository<Space, String> {

    List<Space> findTop5ByOrderByCreationDateDesc();

    List<Space> findAllBySpaceStatus(SpaceStatus spaceStatus);

    List<Space> findAllByImageId(String imageId);
    
    @Query("SELECT d from Space d WHERE d.name like %?1% or d.description like %?1%")
    public List<IVSpaceElement> getContainingSpaces(String searchText);

}