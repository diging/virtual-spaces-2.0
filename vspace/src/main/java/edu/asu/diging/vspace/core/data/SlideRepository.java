package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;

@Repository
@JaversSpringDataAuditable
public interface SlideRepository extends PagingAndSortingRepository<Slide, String> {

    @Query("SELECT d FROM Slide d WHERE d.module.id = ?1")
    public List<Slide> findSlidesForModule(String moduleId);

    @Query("SELECT d.sequence FROM Slide d WHERE d.id = ?1")
    public List<Sequence> getSequencesForSlide(String slideId);

    Page<ISlide> findDistinctByNameContainingOrDescriptionContaining(Pageable requestedPage, String name,
            String description);
    
    @Query("SELECT distinct l FROM Slide s JOIN s.slideNames l WHERE l.exhibitionLanguage=?2 AND s=?1")
    public LocalizedText findNamesBySlideAndExhibitionLanguage(Slide slide, ExhibitionLanguage language);

    @Query("SELECT distinct l FROM Slide s JOIN s.slideDescriptions l WHERE l.exhibitionLanguage=?2 AND s=?1")
    public LocalizedText findDescriptionsBySlideAndExhibitionLanguage(Slide slide, ExhibitionLanguage language);

}
