package edu.asu.diging.vspace.core.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Repository
@JaversSpringDataAuditable
public interface TextContentBlockRepository  extends PagingAndSortingRepository<TextBlock, String> {

    ITextBlock save(ITextBlock textBlock);

    ArrayList<IContentBlock> findBySlideOrderByBlockInOrder(Slide slide);

    List<ITextBlock> findBySlideId(String slideId);

}
