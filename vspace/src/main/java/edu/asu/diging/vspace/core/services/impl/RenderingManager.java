package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.file.IStorageManager;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IRenderingManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Service
public class RenderingManager implements IRenderingManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
       
    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private SlideManager slideManager;

    @Autowired
    private ISequenceManager sequenceManager;
    
    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;
    
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private ISpaceDisplayManager spaceDisplayManager;

    @Autowired
    private IExhibitionManager exhibitManager;

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IStorageManager storageManager;
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    private final String IMAGES_FOLDER_NAME = "images";
       
    private final String SPACE_TEMPLATE = "exhibition/downloads/spaceDownloadTemplate";
    
    private final String SLIDE_TEMPLATE = "exhibition/downloads/slideDownloadTemplate";
    
    private final String FILE_EXTENSION = ".html"; 
    
    private final String PAGE_404 = "exhibition/downloads/page404Template";
    
    /**
     * 
     * Creates a snapshot of the given space and related modules into exhibitionFolderPath
     * 
     * @param space                            the space object 
     * @param exhibitionFolderName             the folder name of the exhibition where space contents will be stored           
     * @param sequenceHistory                  the sequence history object having the history of sequences                        
     * @throws FileStorageException 
     */
    public void createSpaceSnapshot(Space space, String exhibitionFolderName,  SequenceHistory sequenceHistory) throws FileStorageException {
        
        String spaceId = space.getId();
        String spaceFolderPath = exhibitionFolderName + File.separator + spaceId;
        storageEngineDownloads.createFolder(spaceFolderPath);
        
        byte[] fileContent = renderSpace(spaceId, sequenceHistory);
        storageEngineDownloads.storeFile(fileContent, spaceId+FILE_EXTENSION,spaceFolderPath );
        
        String imagesFolderPath = spaceFolderPath + File.separator  + IMAGES_FOLDER_NAME;
        storageEngineDownloads.createFolder(imagesFolderPath); 

        // Copies the space image
        storageManager.copyImage(space.getImage(), imagesFolderPath) ;

        List<IModuleLink> moduleLinks = space.getModuleLinks();

        moduleLinks.forEach(moduleLink -> {
            IModule module =   moduleLink.getModule();
            createModuleSnapshot(module, space,  imagesFolderPath, spaceFolderPath);
        });
    }
    
    /**
     * Renders the given space.
     * 
     * @param spaceId                the space id to be rendered
     * @param sequenceHistory        the sequence history object having the history of sequences 
     * @return byte array as a rendered space content
     * @throws FileStorageException 
     */
    private byte[] renderSpace(String spaceId, SequenceHistory sequenceHistory) throws FileStorageException {

        Context thymeleafContext = new Context();
        populateContextForSpace(thymeleafContext, spaceId, sequenceHistory);
        // add attributes to context
        String response = springTemplateEngine.process(SPACE_TEMPLATE, thymeleafContext);
        return response.getBytes();
    }

    /**
     * 
     * Creates a snapshot of the given module and related slides into space folder path.
     * 
     * @param module            the {@link IModule} object indicating the module
     * @param space             the {@link ISpace} object 
     * @param imagesFolderPath  the path of the folder where the module's images will be stored
     * @param spaceFolderPath   the path of the folder where the module's content will be stored
     * 
     */
    private void createModuleSnapshot(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath) {
        ISequence startSequence = module.getStartSequence();
        if(startSequence!= null) {
            try {
                createSequencesSnapshot(startSequence, module, space, spaceFolderPath,imagesFolderPath );
            } catch (FileStorageException e) {
                logger.error("Could not download Module",e);
            }
        }
    }
       
    /**
     * 
     * Creates snapshot of the given sequence into spacefolderPath.
     * 
     * @param startSequence      the {@link ISequence} object indicating the start sequence of the module
     * @param module             the {@link IModule} object
     * @param space              the {@link ISpace} object
     * @param spaceFolderPath    the space folder path where space content will be stored
     * @param imagesFolderPath   the images folder path where images will be stored
     * @throws FileStorageException
     * 
     */
    private void createSequencesSnapshot(ISequence sequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath) throws FileStorageException {
        List<ISlide> slides = sequence.getSlides();
        slides.forEach(slide -> {
            createSlideSnapshot(slide, sequence, module, space, spaceFolderPath, imagesFolderPath);
            if(slide instanceof BranchingPoint) {              
                ((BranchingPoint) slide).getChoices().forEach(choice -> {

                    if(!choice.getSequence().getId().equals(sequence.getId())) {
                        try {
                            createSequencesSnapshot(choice.getSequence(), module, space, spaceFolderPath, imagesFolderPath);
                        } catch (FileStorageException e) {
                            logger.error("Could not download Sequence",e);
                        } 
                    }
                });
            }
        });            
    }
    
    private void createSlideSnapshot(ISlide slide, ISequence sequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath){
        List<IContentBlock> contentBlocks = slide.getContents();
        contentBlocks.forEach(contentBlock -> {
            if(contentBlock!= null) {
                if(contentBlockManager.getImageBlock(contentBlock.getId())!=null) {
                    IVSImage image = contentBlockManager.getImageBlock(contentBlock.getId()).getImage();//slide.getFirstImageBlock().getImage();
                    try {
                        storageManager.copyImage(image, imagesFolderPath);
                    } catch (FileStorageException e) {
                        logger.error("Could not download Sequence",e);
                    }
                }
                
            }
        });
        try {
            String slideId = slide.getId();
            byte[] fileContent = renderSlide(slideId, space.getId(), module.getId(), sequence.getId());
            storageEngineDownloads.storeFile(fileContent, slideId+FILE_EXTENSION,spaceFolderPath );
        } catch (FileStorageException e) {
            logger.error("Could not store template for the slide", e);
        }
    }

    /**
     * 
     * Renders a slide to a byte array.
     * 
     * @param slideId                the slide id       
     * @param spaceId                the id of the space to be rendered 
     * @param moduleId               the id of the module containing the slide
     * @param sequenceId             the id of the sequence containing the slide
     * @throws FileStorageException 
     */
    private byte[] renderSlide(String slideId, String spaceId, String moduleId, String sequenceId ) throws FileStorageException {
        Context thymeleafContext = new Context();
        try {
            populateContextForSlide( thymeleafContext, spaceId, moduleId, sequenceId, slideId );            
            return springTemplateEngine.process(SLIDE_TEMPLATE, thymeleafContext).getBytes();
        } catch (SlidesInSequenceNotFoundException  | SequenceNotFoundException |SlideNotFoundException e ) {
            logger.error("Could not add html page for slide" , e);
            return springTemplateEngine.process(PAGE_404, thymeleafContext).getBytes();
        }
    }
    
    /**
     *@see IRenderingManager#populateContextForSlide(Context, String, String, String, String)
     */
    @Override
    public void populateContextForSlide(Context context, String spaceId, String moduleId, String sequenceId, String slideId) 
            throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException {
        IModule module = moduleManager.getModule(moduleId);
        context.setVariable("module", module);
        
        String startSequenceId = module.getStartSequence().getId();
        context.setVariable("startSequenceId", startSequenceId);
        
        ISequence sequenceExist=moduleManager.checkIfSequenceExists(moduleId, sequenceId);
        if (sequenceExist==null) {            
            throw new SequenceNotFoundException(sequenceId);
        }
        
        List<ISlide> sequenceSlides = sequenceManager.getSequence(sequenceId).getSlides();
        boolean slideExist = sequenceSlides.stream().anyMatch(slide -> slide.getId().equals(slideId));
        if (!slideExist) {
            throw new SlideNotFoundException(slideId);
        }

        if (sequenceSlides.size() == 0) {
            throw new SlidesInSequenceNotFoundException();
        }
        context.setVariable("firstSlide", module.getStartSequence().getSlides().get(0).getId());
        
        ISlide currentSlide = slideManager.getSlide(slideId);
        int slideIndex = sequenceSlides.indexOf(currentSlide);

        int slideSize = sequenceSlides.size();
        
        String prevSlideId = (slideIndex > 0) ? sequenceSlides.get(slideIndex - 1).getId() : "";
        String nextSlideId = (slideSize > slideIndex + 1) ? sequenceSlides.get(slideIndex + 1).getId() : "";

        context.setVariable("sequences",moduleManager.getModuleSequences(moduleId));
        context.setVariable("sequence",sequenceExist);
        context.setVariable("slides", sequenceSlides);
        context.setVariable("currentSequenceId", sequenceId);
        context.setVariable("nextSlide", nextSlideId);
        context.setVariable("prevSlide", prevSlideId);
        context.setVariable("currentSlideCon", currentSlide);
        context.setVariable("numOfSlides", sequenceSlides.size());
        context.setVariable("currentNumOfSlide", slideIndex + 1);
        context.setVariable("spaceId", spaceId);
        context.setVariable("spaceName", spaceManager.getSpace(spaceId).getName());
    }
    
    /** Populates context with variables to process space template
     * 
     * @param context
     * @param id
     */
    @Override
    public void populateContextForSpace(Context context, String id, SequenceHistory sequenceHistory) {

        ISpace space = spaceManager.getSpace(id);
        List<ISpaceLinkDisplay> spaceLinks;
        context.setVariable("isSpacePublished", true);
        IExhibition exhibition = exhibitManager.getStartExhibition();
        context.setVariable("exhibitionConfig", exhibition);
        context.setVariable("space", space);
        context.setVariable("moduleList", moduleLinkManager.getLinkDisplays(id));
        if (space.isShowUnpublishedLinks()) {
            spaceLinks = spaceLinkManager.getLinkDisplays(id);
        } else {
            spaceLinks = spaceLinkManager.getSpaceLinkForGivenOrNullSpaceStatus(id, SpaceStatus.PUBLISHED);
        }
        List<ISpaceLinkDisplay> filteredSpaceLinks = spaceLinks.stream().filter(
                spaceLinkDisplayObj -> !spaceLinkDisplayObj.getLink().getTargetSpace().isHideIncomingLinks())
                .collect(Collectors.toList());
        context.setVariable("spaceLinks", filteredSpaceLinks);
        context.setVariable("display", spaceDisplayManager.getBySpace(space));
        context.setVariable("externalLinkList", externalLinkManager.getLinkDisplays(id));
    }
}