package edu.asu.diging.vspace.core.services.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;


@Service
public class DownloadsManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SpaceRepository spaceRepository;
    
    @Autowired
    StorageEngine storageEngine;
    
    @Value("${downloads_path}")
    private String downloadsPath;
    
    @Autowired
    SpringTemplateEngine springTemplateEngine;
    
    
    @Autowired
    private ISpaceManager spaceManager;

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
    private IModuleManager moduleManager;

    @Autowired
    private SlideManager slideManager;

    @Autowired
    private ISequenceManager sequenceManager;
    

    @Autowired
    private SequenceHistory sequenceHistory;
       
    
    @Autowired
    private ExhibitionDownloadRepository exhibitionDownloadRepo;
    
    public byte[] downloadExhibition(String resourcesPath, String exhibitionFolderName, WebContext context) throws IOException {       
        byte[] resource = null;
        String exhibitionFolderPath =  storageEngine.createFolder(exhibitionFolderName, downloadsPath);
        copyResourcesToExhibition(exhibitionFolderPath,resourcesPath ); 

        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        for(Space space : spaces) {
            downloadSpace(space, exhibitionFolderPath, context);                
        }               
        resource = storageEngine.generateZipFolder(exhibitionFolderPath);
        exhibitionDownloadRepo.save( new ExhibitionDownload(exhibitionFolderPath));
        return resource;
    }

    public void downloadSpace(Space space, String exhibitionFolderPath, WebContext context) {

        String spaceFolderPath = storageEngine.createFolder(space.getId(), exhibitionFolderPath);

        addHtmlPageForSpace(space.getId(), spaceFolderPath , context);

        String imagesFolderPath = storageEngine.createFolder("images" , spaceFolderPath);

        copyImageToFolder(space.getImage(),imagesFolderPath) ;

        List<IModuleLink> moduleLinks = space.getModuleLinks();

        moduleLinks.forEach(moduleLink -> {
                     
            IModule module =   moduleLink.getModule();
            downloadModule(module, space,  imagesFolderPath, spaceFolderPath,  context);
            
        });        
    }

    public void downloadModule(IModule module, ISpace space, String imagesFolderPath, String spaceFolderPath, WebContext context) {
        ISequence startSequence = module.getStartSequence();
        if(startSequence!= null) {
            downloadSequence(startSequence, module, space, spaceFolderPath,imagesFolderPath , context);
           
        }

    }

    public void downloadSequence(ISequence startSequence, IModule module, ISpace space, String spaceFolderPath,
            String imagesFolderPath,  WebContext context) {
        List<ISlide> slides = startSequence.getSlides();
        slides.forEach(slide -> {
            if(slide instanceof BranchingPoint) {
                BranchingPoint branchingPoint = (BranchingPoint) slide;
                List<IChoice> choices  =  branchingPoint.getChoices();
                choices.forEach(choice -> {
                    if(!choice.getSequence().getId().equals(startSequence.getId())) {
                        downloadSequence(choice.getSequence(), module, space, spaceFolderPath, imagesFolderPath,  context); 
                    }
                });
            }
            else {
                IVSImage image = slide.getFirstImageBlock().getImage();
                copyImageToFolder(image, imagesFolderPath);
                addHtmlPageForSlide(slide.getId(), spaceFolderPath ,  context, space.getId(), module.getId(), startSequence.getId());
            }

        });
        
    }

    private void addHtmlPageForSlide(String slideId, String spaceFolderPath, WebContext context,String spaceId, String moduleId, String sequenceId ) {
        try {      
            populateContextForSlide( context, spaceId, moduleId, sequenceId, slideId );
            String response = springTemplateEngine.process("exhibition/downloads/slideDownloadTemplate" , context);
            byte[] fileContent = response.getBytes();
            storageEngine.storeFile(fileContent, slideId+".html",null, spaceFolderPath );

        } catch ( FileStorageException e) {
            logger.error("Could not add html page for slide" , e);
        } catch (SlidesInSequenceNotFoundException  | SequenceNotFoundException |SlideNotFoundException e ) {
            logger.error("Could not add html page for slide" , e);
        }       
    }

    private void populateContextForSlide(WebContext context, String spaceId, String moduleId, String sequenceId, String slideId) throws SlidesInSequenceNotFoundException, SequenceNotFoundException, SlideNotFoundException {

        ISpace space = spaceManager.getSpace(spaceId);
//        if (space == null) {
//            return "redirect:/exhibit/404";
//        }
        IModule module = moduleManager.getModule(moduleId);
        context.setVariable("module", module);
//        if (module == null) {
//            return "redirect:/exhibit/404";
//        }
//        if (module.getStartSequence() == null) {
//            context.setVariable("showAlert", true);
//            context.setVariable("message", "Sorry, module has not been configured yet.");
//            return "/exhibition/module";
//        }
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
        String nextSlideId = "";
        String prevSlideId = "";

        ISlide currentSlide = slideManager.getSlide(slideId);
        int slideIndex = sequenceSlides.indexOf(currentSlide);

        int slideSize = sequenceSlides.size();
        if (slideSize > slideIndex + 1) {
            nextSlideId = sequenceSlides.get(slideIndex + 1).getId();
        }
        if (slideIndex > 0) {
            prevSlideId = sequenceSlides.get(slideIndex - 1).getId();
        }
        context.setVariable("sequences",moduleManager.getModuleSequences(moduleId));
        context.setVariable("sequence",sequenceExist);
        context.setVariable("slides", sequenceSlides);
        context.setVariable("currentSequenceId", sequenceId);
        context.setVariable("nextSlide", nextSlideId);
        context.setVariable("prevSlide", prevSlideId);

        context.setVariable("currentSlideCon", currentSlide);
//        if(currentSlide instanceof BranchingPoint) {
//            context.setVariable("choices", ((BranchingPoint)currentSlide).getChoices());
//            if(back && sequenceHistory.peekBranchingPointId().equalsIgnoreCase(slideId)) {
//                sequenceHistory.popFromHistory();
//            }
//        }
//        if(branchingPointId!=null && !branchingPointId.isEmpty()){
//            sequenceHistory.addToHistory(previousSequenceId,branchingPointId);
//        }
        if(sequenceHistory.hasHistory()) {
            context.setVariable("showBackToPreviousChoice", true);
            context.setVariable("previousSequenceId", sequenceHistory.peekSequenceId());
            context.setVariable("previousBranchingPoint", ((BranchingPoint)slideManager.getSlide(sequenceHistory.peekBranchingPointId())));
        }

        context.setVariable("numOfSlides", sequenceSlides.size());
        context.setVariable("currentNumOfSlide", slideIndex + 1);
        context.setVariable("spaceId", spaceId);
        context.setVariable("spaceName", spaceManager.getSpace(spaceId).getName());
        
        
    }

    public void copyResourcesToExhibition(String exhibitionFolderPath, String resourcesPath) {

        try {
            FileUtils.copyDirectory(new File(resourcesPath), new File(exhibitionFolderPath+ File.separator + "resources")); //TODO: constant
        } catch (IOException e) {
            logger.error("Could not copy resources" , e);
        } 
    }


    public void copyImageToFolder(IVSImage image, String imagesFolderPath) {
        try {
            byte[] byteArray = storageEngine.getImageContent(image.getId(), image.getFilename());
            storageEngine.storeFile(byteArray, image.getFilename(),image.getId(), imagesFolderPath );

        } catch (IOException | FileStorageException e) {
            logger.error("Could not copy images" , e);
        }     
    }


    public void addHtmlPageForSpace(String directory, String spaceFolderPath,  WebContext context ) {
        try {      
            populateContextForSpace( context, directory);
            String response = springTemplateEngine.process("exhibition/downloads/spaceDownloadTemplate" , context);
            byte[] fileContent = response.getBytes();
            storageEngine.storeFile(fileContent, directory+".html",null, spaceFolderPath );
         
        } catch ( FileStorageException e) {
            logger.error("Could not copy template" , e);
        }   
      
    }
    
    
    private void populateContextForSpace(WebContext context, String id) {
        

        ISpace space = spaceManager.getSpace(id);
        List<ISpaceLinkDisplay> spaceLinks;
        Boolean isSpacePublished = true;
        /* (non-Javadoc)
         * Below null check is added to accommodate already existing spaces with null space status
         */
//        if (space.getSpaceStatus() != null && space.getSpaceStatus().equals(SpaceStatus.UNPUBLISHED)) {
//            if (authenticationFacade.getAuthenticatedUser() != null) {
//                isSpacePublished = false;
//            } else {
//                return "redirect:/exhibit/404";
//            }
//        }
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

        if (sequenceHistory.hasHistory()) {
            sequenceHistory.flushFromHistory();
        }
    }

    public byte[] downloadExhibitionFolder(String id) throws Exception {
        Optional<ExhibitionDownload> exhibitionDownlaod = exhibitionDownloadRepo.findById(id);

        if(exhibitionDownlaod.isPresent()) {
            return  storageEngine.generateZipFolder(exhibitionDownlaod.get().getFolderPath());                
        }else {
            throw new Exception("Exhibition folder not found");
        }

    }



}
