package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.impl.DefaultImages;

@Repository
@JaversSpringDataAuditable
public interface DefaultImageRepository extends PagingAndSortingRepository<DefaultImages, String> {

    List<DefaultImages> findByFilenameLikeOrNameLike(String filename, String name);

    List<DefaultImages> findByFilenameLike(String filename);

    Page<DefaultImages> findByCategories(Pageable pageable, ImageCategory category);

    List<IVSImage> findByCategories(ImageCategory category);
    
    long countByCategories(ImageCategory category);



}
