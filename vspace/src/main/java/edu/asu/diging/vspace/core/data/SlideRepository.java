package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;

@Repository
@JaversSpringDataAuditable
public interface SlideRepository extends PagingAndSortingRepository<Slide, String> {

    @Query("SELECT d FROM Slide d WHERE d.module.id = ?1")
    public List<Slide> findSlidesForModule(String moduleId);

    @Query("SELECT d.sequence FROM Slide d WHERE d.id = ?1")
    public List<Sequence> getSequencesForSlide(String slideId);

    Page<Slide> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);

    List<Slide> findDistinctByNameContainingOrDescriptionContaining(String name, String description);

}
