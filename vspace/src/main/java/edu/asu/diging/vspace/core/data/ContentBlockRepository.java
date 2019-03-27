package edu.asu.diging.vspace.core.data;

import java.util.ArrayList;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;

@Repository
@JaversSpringDataAuditable
public interface ContentBlockRepository extends PagingAndSortingRepository<ContentBlock, String> {

//    public ArrayList<IContentBlock> findBySlide(Slide slide);

}
