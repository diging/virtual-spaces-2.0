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
    
    private final String IMAGES_FOLDER_NAME = "images";
       
    private final String SPACE_TEMPLATE_DOWNLOAD_API = "exhibition/downloads/spaceDownloadTemplate";
    
    private final String SLIDE_TEMPLATE_DOWNLOAD_API = "exhibition/downloads/slideDownloadTemplate";
    
    private final String DOWNLOAD_FILE_EXTENSION = ".html"; 
    
    /**
     * 
     * Download given space and related modules into exhibitionFolderPath
     * @param space                            the space object 
     * @param exhibitionFolderName             the folder name of the exhibition where space contents will be stored           
     * @param sequenceHistory                  the sequence history object having the history of sequences                        
     * @throws FileStorageException 
     */
    public void downloadSpace(Space space, String exhibitionFolderName,  SequenceHistory sequenceHistory) throws FileStorageException {

        String spaceFolderPath = exhibitionFolderName + File.separator + space.getId();
        storageEngineDownloads.createFolder(spaceFolderPath);

        renderSpace(space.getId(), spaceFolderPath , sequenceHistory);

        String imagesFolderPath = spaceFolderPath + File.separator  + IMAGES_FOLDER_NAME;
        storageEngineDownloads.createFolder(imagesFolderPath); 

        // Copies the space image
        storageManager.copyImage(space.getImage(), imagesFolderPath) ;

        List<IModuleLink> moduleLinks = space.getModuleLinks();

        moduleLinks.forEach(moduleLink -> {
            IModule module =   moduleLink.getModule();
            downloadModule(module, space,  imagesFolderPath, spaceFolderPath);
        });
    }
    
    /**
     * Stores the processed template for space into spaceFolderPath.
     * 
     * @param spaceId                the space id to be rendered
     * @param spaceFolderPath        the folder where the space content will be stored
     * @param sequenceHistory        the sequence history object having the history of sequences 
     * @throws FileStorageException 
     */
    private void renderSpace(String spaceId, String spaceFolderPath,  SequenceHistory sequenceHistory) throws FileStorageException {

        Context thymeleafContext = new Context();
        populateContextForSpace(thymeleafContext, spaceId, sequenceHistory);
        // add attributes to context
        String response = springTemplateEngine.process(SPACE_TEMPLATE_DOWNLOAD_API, thymeleafContext);
        byte[] fileContent = response.getBytes();
        storageEngineDownloads.storeFile(fileContent, spaceId+DOWNLOAD_FILE_EXTENSION,spaceFolderPath );
    }

    /**
     * 
     * Downloads given module and related slides into space folder path.
     * @param module            the {@link IModule} object indicating the module
     * @param space             the {@link ISpace} object 
     * @param imagesFolderPath  the path of the folder where the module's images will be stored
     * @param spaceFolderPath   he path of the folder where the module's content will be stored
     * 
     */
    private void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath) {
        ISequence startSequence = module.getStartSequence();
        if(startSequence!= null) {
            try {
                downloadSequences(startSequence, module, space, spaceFolderPath,imagesFolderPath );
            } catch (FileStorageException e) {
                logger.error("Could not download Module",e);
            }
        }
    }
       
    /**
     * 
     * Downloads given sequence into spacefolderPath.
     * @param startSequence      the {@link ISequence} object indicating the start sequence of the module
     * @param module             the {@link IModule} object
     * @param space              the {@link ISpace} object
     * @param spaceFolderPath    the space folder path where space content will be stored
     * @param imagesFolderPath   the images folder path where images will be stored
     * @throws FileStorageException
     * 
     */
    private void downloadSequences(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath) throws FileStorageException {
        List<ISlide> slides = startSequence.getSlides();
        slides.forEach(slide -> {
            if(slide instanceof BranchingPoint) {
                BranchingPoint branchingPoint = (BranchingPoint) slide;
                List<IChoice> choices  =  branchingPoint.getChoices();
                choices.forEach(choice -> {

                    if(!choice.getSequence().getId().equals(startSequence.getId())) {

                        try {
                            downloadSequences(choice.getSequence(), module, space, spaceFolderPath, imagesFolderPath);
                        } catch (FileStorageException e) {
                            logger.error("Could not download Sequence",e);
                        } 
                    }
                });
            } else {
                IContentBlock contentBlock = slide.getFirstImageBlock();
                if(contentBlock!= null) {
                    IVSImage image = slide.getFirstImageBlock().getImage();
                    try {
                        storageManager.copyImage(image, imagesFolderPath);
                    } catch (FileStorageException e) {
                        logger.error("Could not download Sequence",e);
                    }
                }
                try {
                    renderSlide(slide.getId(), spaceFolderPath ,  space.getId(), module.getId(), startSequence.getId());
                } catch (FileStorageException e) {
                    logger.error("Could not store template for the slide", e);
                }
            }
        });
    }

    /**
     * 
     * Stores the processed template for slide into spaceFolderPath
     * @param slideId                the slide Id 
     * @param spaceFolderPath        the space folder path where space content will be stored             
     * @param spaceId                the id of the space to be rendered 
     * @param moduleId
     * @param sequenceId
     * @throws FileStorageException 
     */
    private void renderSlide(String slideId, String spaceFolderPath, String spaceId, String moduleId, String sequenceId ) throws FileStorageException {
        try {      
            Context thymeleafContext = new Context();

            populateContextForSlide( thymeleafContext, spaceId, moduleId, sequenceId, slideId );
            String response = springTemplateEngine.process(SLIDE_TEMPLATE_DOWNLOAD_API , thymeleafContext);
            byte[] fileContent = response.getBytes();
            storageEngineDownloads.storeFile(fileContent, slideId+DOWNLOAD_FILE_EXTENSION,spaceFolderPath );

        } catch (SlidesInSequenceNotFoundException  | SequenceNotFoundException |SlideNotFoundException e ) {
            logger.error("Could not add html page for slide" , e);
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
        Boolean isSpacePublished = true;

        context.setVariable("isSpacePublished", isSpacePublished);
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