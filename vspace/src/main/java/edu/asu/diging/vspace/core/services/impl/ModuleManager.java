package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ModuleDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISlideFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Transactional
@Service
public class ModuleManager implements IModuleManager {

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private ISlideFactory slideFactory;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#storeModule(edu.asu.
     * diging.vspace.core.model.IModule, java.lang.String)
     */
    @Override
    public IModule storeModule(IModule module) {
        return moduleRepo.save((Module) module);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#getModule(java.lang.
     * String)
     */
    @Override
    public IModule getModule(String id) {
        Optional<Module> module = moduleRepo.findById(id);
        if (module.isPresent()) {
            return module.get();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#storeSlide(java.lang.
     * String, java.lang.String, java.lang.String)
     */
    @Override
    public ISlide createSlide(String id, String title, String description) throws ModuleDoesNotExistException {
        IModule module = getModule(id);
        ISlide slide = slideFactory.createSlide(module, title, description);
        return slide;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#storeSlide(edu.asu.
     * diging.vspace.core.model.ISlide, java.lang.String)
     */
    public CreationReturnValue storeSlide(ISlide slide, byte[] image, String filename) {
        IVSImage bgImage = null;
        if (image != null && image.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(image);

            bgImage = imageFactory.createImage(filename, contentType);
            bgImage = imageRepo.save((VSImage) bgImage);
        }

        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());

        if (bgImage != null) {
            String relativePath = null;
            try {
                relativePath = storage.storeFile(image, filename, bgImage.getId());
            } catch (FileStorageException e) {
                returnValue.getErrorMsgs().add("Background image could not be stored: " + e.getMessage());
            }
            bgImage.setParentPath(relativePath);
            imageRepo.save((VSImage) bgImage);
            slide.setImage(bgImage);
        }

        slide = slideRepo.save((Slide) slide);
        returnValue.setElement(slide);
        return returnValue;
    }

    @Override
    public List<ISlide> getModuleSlides(String moduleId) {
        IModule module = getModule(moduleId);
        return new ArrayList<>(slideRepo.findByModule((Module) module));
    }

    @Override
    public ISlide getSlide(String slideId) {
        Optional<Slide> slide = slideRepo.findById(slideId);
        if (slide.isPresent()) {
            return slide.get();
        }
        return null;
    }
}
