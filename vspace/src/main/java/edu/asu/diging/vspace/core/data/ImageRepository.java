package edu.asu.diging.vspace.core.data;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.impl.VSImage;

@Repository
@JaversSpringDataAuditable
public interface ImageRepository extends PagingAndSortingRepository<VSImage, String> {

    List<VSImage> findByFilenameLikeOrNameLike(String filename, String name);

    List<VSImage> findByFilenameLikeOrNameLikeOrDescriptionLike(String filename, String name, String description);

    Page<VSImage> findByFilenameLikeOrNameLikeOrDescriptionLike(Pageable pageable, String filename, String name, String description);

    @Query("SELECT i FROM  VSImage i join i.categories c WHERE c=?1 AND (i.filename like ?2 or i.description like ?3 or i.name like ?4)")
    Page<VSImage> findByCategoryAndFilenameLikeOrNameLikeOrDescriptionLike(Pageable pageable, 
            ImageCategory category, String filename, String name, String description);
    
    List<VSImage> findByFilenameLike(String filename);

    Page<VSImage> findByCategories(Pageable pageable, ImageCategory category);

    List<IVSImage> findByCategories(ImageCategory category);
    
    long countByCategories(ImageCategory category);
}