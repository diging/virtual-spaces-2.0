package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.services.ISlideManager;

/**
 * Abstract base class for content block managers providing common functionality
 * for delete and get operations, as well as content order management.
 * 
 * @param <T> The type of content block this manager handles
 * @param <R> The repository type for the content block
 */
@Transactional(rollbackFor = { Exception.class })
public abstract class AbstractContentBlockManager<T extends IContentBlock, R extends JpaRepository<?, String>> {

    @Autowired
    protected ISlideManager slideManager;

    @Autowired
    protected ContentBlockRepository contentBlockRepository;

    /**
     * Abstract method to be implemented by subclasses to return their specific repository.
     * 
     * @return The repository for the specific content block type
     */
    protected abstract R getRepository();

    /**
     * Deletes a content block by ID and updates content order for remaining blocks.
     * 
     * @param blockId The ID of the block to delete
     * @param slideId The ID of the slide containing the block
     * @throws BlockDoesNotExistException if the block doesn't exist
     */
    public void deleteById(String blockId, String slideId) throws BlockDoesNotExistException {
        if (blockId == null) {
            return;
        }
        
        Integer contentOrder = null;
        Optional<ContentBlock> contentBlock = contentBlockRepository.findById(blockId);
        if (contentBlock.isPresent()) {
            contentOrder = contentBlock.get().getContentOrder();
        } else {
            throw new BlockDoesNotExistException("Block Id not present");
        }
        
        try {
            getRepository().deleteById(blockId);
            updateContentOrderAfterDeletion(slideId, contentOrder);
        } catch (EmptyResultDataAccessException e) {
            throw new BlockDoesNotExistException(e);
        }
    }

    /**
     * Retrieves a content block by ID.
     * 
     * @param blockId The ID of the block to retrieve
     * @return The content block or null if not found
     */
    @SuppressWarnings("unchecked")
    public T getById(String blockId) {
        Optional<?> block = getRepository().findById(blockId);
        if (block.isPresent()) {
            return (T) block.get();
        }
        return null;
    }

    /**
     * Calculates the next content order for a new block on the specified slide.
     * 
     * @param slideId The ID of the slide
     * @return The next content order value
     */
    protected Integer getNextContentOrder(String slideId) {
        Integer maxContentOrder = contentBlockRepository.findMaxContentOrder(slideId);
        return maxContentOrder == null ? 0 : maxContentOrder + 1;
    }

    /**
     * Updates content order for blocks after a deletion.
     * Decreases content order by 1 for all blocks with order greater than the deleted block.
     * 
     * @param slideId The ID of the slide
     * @param deletedContentOrder The content order of the deleted block
     */
    private void updateContentOrderAfterDeletion(String slideId, Integer deletedContentOrder) {
        if (deletedContentOrder == null) {
            return;
        }
        
        var contentBlockList = contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan(slideId, deletedContentOrder);
        if (contentBlockList != null) {
            for (ContentBlock eachContentBlock : contentBlockList) {
                eachContentBlock.setContentOrder(eachContentBlock.getContentOrder() - 1);
            }
            contentBlockRepository.saveAll(contentBlockList);
        }
    }
}
