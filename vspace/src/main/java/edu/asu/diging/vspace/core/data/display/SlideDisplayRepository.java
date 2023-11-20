package edu.asu.diging.vspace.core.data.display;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.impl.SlideDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;

public interface SlideDisplayRepository extends PagingAndSortingRepository<SlideDisplay, String>{
    
    @Modifying
    @Query("delete from SlideDisplay where slide_id = ?1")
    void deleteBySlideId(String id);

    public List<SlideDisplay> getBySlide(ISlide slide);

}
