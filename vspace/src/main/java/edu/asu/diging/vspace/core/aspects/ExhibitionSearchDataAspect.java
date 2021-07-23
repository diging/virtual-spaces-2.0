package edu.asu.diging.vspace.core.aspects;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.auth.impl.AuthenticationFacade;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IdPrefix;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Component
@Aspect
public class ExhibitionSearchDataAspect {

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private AuthenticationFacade authFacade;

    @After("execution(public * edu.asu.diging.vspace.web.general..*Controller.*(..))")
    public void setExhibition(JoinPoint jp) {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?> returnType = signature.getReturnType();
        // only if there is a model object injected and if we're returning a string
        // (assuming that returning a string implies rendering of a view afterwards
        if(args!=null && returnType==String.class) {
            for(Object obj : args) {
                if(obj instanceof Model && !(obj instanceof RedirectAttributes)) {
                    if(!((Model) obj).containsAttribute("exhibition")) {
                        ((Model) obj).addAttribute("exhibition", exhibitionManager.getStartExhibition());
                    }
                    if (!((Model) obj).containsAttribute("publishedSpaces")) {
                        List<ISpace> publishedSpaces=spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED);
                        /* (non-Javadoc)
                         * Added to show spaces with null status and accommodate existing spaces with null space status
                         */
                        publishedSpaces.addAll(spaceManager.getSpacesWithStatus(null));
                        ((Model) obj).addAttribute("publishedSpaces", publishedSpaces);
                    }
                }
            }  
        }
    }

    @Around("execution(public * edu.asu.diging.vspace.web.general..*Controller.*(..))")
    public Object showExhibition(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        int indexOfModel = (Arrays.asList(signature.getParameterTypes())).indexOf(Model.class);
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition(); 
        // If there is no exhibition, we go back to root url page.
        if(exhibition==null) {
            return "redirect:/";
        }
        //If no exhibition mode has been setup for existing exhibition, we skip modes and aspects.
        if(exhibition.getMode() == null) {
            return jp.proceed();
        }
        Map<IdPrefix, String> ids = getIds(args, signature);
        String spaceId = ids.getOrDefault(IdPrefix.SPACEID, "");
        String moduleId = ids.getOrDefault(IdPrefix.MODULEID, "");
        return redirectRequest(jp, spaceId, moduleId, indexOfModel, exhibition);
    }


    /**
     * Based on exhibition mode, get the redirect page or pass control to controller. 
     * @param jp            The joinpoint variable, used to fetch request parameters and proceed with the request.
     * @param spaceId       Current spaceId that is requested to be viewed by user.
     * @param moduleId      The moduleId that the user has requested to view.
     * @param modelIndex    Index of the model information in the reuest parameters.
     * @param exhibition    Current exhibition that is being shown to user.
     * @return              returns the page to load upon aspect completion.
     * @throws Throwable    
     */
    private Object redirectRequest(ProceedingJoinPoint jp, String spaceId, String moduleId, int modelIndex, Exhibition exhibition) throws Throwable{
        ISpace space = spaceManager.getSpace(spaceId);
        IModule module = moduleManager.getModule(moduleId);
        Object[] args = jp.getArgs();
        ExhibitionModes exhibitionMode = exhibition.getMode();
        // If exhibition is set to offline, set the custom message or default message.
        if(exhibitionMode.equals(ExhibitionModes.OFFLINE)) {
            String modeValue = exhibition.getCustomMessage().equals("") == false ? exhibition.getCustomMessage() : exhibitionMode.getValue();
            ((Model) args[modelIndex]).addAttribute("modeValue", modeValue);
        }
        // If exhibition is set to maintenance, set the default message.
        if(exhibitionMode.equals(ExhibitionModes.MAINTENANCE)) {
            ((Model) args[modelIndex]).addAttribute("modeValue", exhibitionMode.getValue());
        }
        // If user is not logged in and exhibition is not active, show maintenance page.
        if(authFacade.getAuthenticatedUser()==null && !exhibitionMode.equals(ExhibitionModes.ACTIVE)) {
            return "maintenance";
        }
//        if((space!=null || module==null) && (space==null || module!=null)) {
//            return "redirect:/exhibit/search";
//        }
        // If the space and module Id is not found, show message on screen.
//        if(space==null && module==null) {
//            return "redirect:/exhibit/404";
//        }
        // If user is logged in and exhibition is not active, show exhibition with pop up message.
        if(authFacade.getAuthenticatedUser()!=null && !exhibitionMode.equals(ExhibitionModes.ACTIVE)) {
            return jp.proceed();
        }
        return jp.proceed();
    }


    /**
     * Method to get the space or module id's from the request body.
     * @param args      The request parameters from the original request.
     * @param signature Contains the information about the arguments present in args.
     * @return          A map with spaceId and moduleId from the request args.
     */
    private Map<IdPrefix, String> getIds(Object[] args, MethodSignature signature) { 
        Map<IdPrefix, String> res = new HashMap<>();
        for(int i=0; i <signature.getParameterTypes().length; ++i) {
            if(signature.getParameterTypes()[i].equals(String.class)) {
                if(args[i] != null && ((String) args[i]).length() > 2) {
                    String prefix = ((String) args[i]).substring(0,3);
                    if(prefix.equalsIgnoreCase(IdPrefix.SPACEID.getValue())) {
                        res.put(IdPrefix.SPACEID, (String) args[i]);
                    }
                    if(prefix.equalsIgnoreCase(IdPrefix.MODULEID.getValue())) {
                        res.put(IdPrefix.MODULEID, (String) args[i]);
                    }
                }
            }
        }
        return res;
    }
}
