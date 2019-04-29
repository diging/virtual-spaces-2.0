package edu.asu.diging.vspace.core.data;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;

@Repository
@JaversSpringDataAuditable
public interface ImageContentBlockRepository extends PagingAndSortingRepository<ImageBlock, String> {

}
