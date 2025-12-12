package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.services.IGenericContentBlockManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional(rollbackFor = { Exception.class })
public abstract class GenericContentBlockManager<T extends IContentBlock, R extends CrudRepository<?, String>> 
        implements IGenericContentBlockManager<T> {

    @Autowired
    protected ISlideManager slideManager;

    @Autowired
    protected ContentBlockRepository contentBlockRepository;

    protected abstract R getRepository();

    public abstract T createContentBlock(String slideId);

    @Override
    @SuppressWarnings("unchecked")
    public T getContentBlock(String blockId) {
        Optional<?> block = getRepository().findById(blockId);
        if (block.isPresent()) {
            return (T) block.get();
        }
        return null;
    }

    @Override
    public void deleteContentBlock(String blockId, String slideId) throws BlockDoesNotExistException {
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

    protected Integer getNextContentOrder(String slideId) {
        Integer maxContentOrder = contentBlockRepository.findMaxContentOrder(slideId);
        return maxContentOrder == null ? 0 : maxContentOrder + 1;
    }

    private void updateContentOrderAfterDeletion(String slideId, Integer deletedContentOrder) {
        if (deletedContentOrder == null) {
            return;
        }
        
        List<ContentBlock> contentBlockList = contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan(slideId, deletedContentOrder);
        if (contentBlockList != null) {
            for (ContentBlock eachContentBlock : contentBlockList) {
                eachContentBlock.setContentOrder(eachContentBlock.getContentOrder() - 1);
            }
            contentBlockRepository.saveAll(contentBlockList);
        }
    }
}
