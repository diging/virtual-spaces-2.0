package edu.asu.diging.vspace.core.data;

import java.util.List;
import java.util.Set;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

@Repository
@JaversSpringDataAuditable
public interface SlideRepository extends PagingAndSortingRepository<Slide, String> {

    @Query("SELECT d FROM Slide d WHERE d.module.id = ?1")
    public List<Slide> findSlidesForModule(String moduleId);

    @Query("SELECT d.sequence FROM Slide d WHERE d.id = ?1")
    public List<Sequence> getSequencesForSlide(String slideId);

    Page<ISlide> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);
    
    @Query(value="SELECT d.Slide_Id FROM Sequence_Slides d, Module m WHERE d.Sequence_Id = m.startSequence_id", nativeQuery = true)
    Set<String> findAllSlidesFromStartSequences();
    
    @Query("SELECT DISTINCT s from Slide s where  s.module.id IN "
            + "(SELECT DISTINCT module.id from Module module JOIN ModuleLink moduleLink ON module.id = moduleLink.module.id JOIN Space space "
            + "on space.id = moduleLink.space.id WHERE space.spaceStatus = ?3 AND module.moduleStatus = ?4) "
            + "AND s.id IN (Select d.id from Sequence ss join ss.slides d , Module m where m.startSequence.id = ss.id)"
            + "AND s.name like %?1% OR s.description like %?2%")
    Page<ISlide> findDistinctSlidesInSpaceContainingNameOrDescription(Pageable requestedPage, String name,
            String description, SpaceStatus spaceStatus, ModuleStatus moduleStatus);

}
