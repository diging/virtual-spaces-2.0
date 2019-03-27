package edu.asu.diging.vspace.core.data;

import java.util.ArrayList;
import java.util.Collection;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Repository
@JaversSpringDataAuditable
public interface ImageContentBlockRepository  extends PagingAndSortingRepository<ImageBlock, String> {

    IImageBlock save(IImageBlock imageBlock);

//    ArrayList<ImageBlock> findBySlide(Slide slide);

}
