package edu.asu.diging.vspace.core.aspects;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
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
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Component
@Aspect
public class ExhibitionDataAspect {

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private AuthenticationFacade authFacade;

    @After("execution(public * edu.asu.diging.vspace.web..*Controller.*(..))")
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

    @Around("execution(public * edu.asu.diging.vspace.web.publicview..*Controller.*(..))")
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
        Map<String, String> ids = getIds(args, signature);
        String spaceId = ids.getOrDefault(IdPrefix.SPACEID.getValue(), "");
        String moduleId = ids.getOrDefault(IdPrefix.MODULEID.getValue(), "");
        return redirectRequest(jp, spaceId, moduleId, indexOfModel, exhibition);
    }
    
    // Based on exhibition mode, get the redirect page or pass control to controller.  
    public Object redirectRequest(ProceedingJoinPoint jp, String spaceId, String moduleId, int modelIndex, Exhibition exhibition) throws Throwable{
        ISpace space = spaceManager.getSpace(spaceId);
        IModule module = moduleManager.getModule(moduleId);
        Object[] args = jp.getArgs();
        // For exhibition being active, if space or module is not found, show not found page or else give control to controller.
        if(exhibition.getMode().equals(ExhibitionModes.ACTIVE)) {
            if(space==null || module==null) {
                return "notFound";
            }
            return jp.proceed();
        }
        // If the user is logged in, if space or module invalid, show not found page or else add attribute to show pop up
        // notifying user about status of exhibition. ModelIndex checks if there is a model to add attribute to.
        if(authFacade.getAuthenticatedUser()!=null && modelIndex>-1) {
            if(space==null && module==null) {
                return "notFound";
            }
            ((Model) args[modelIndex]).addAttribute("showModal", "true"); 
            return jp.proceed();
        }
        // When exhibition is offline, if custom message is setup, show that to user, or else show default message, or else
        // show maintenance default message if exhibition is set to maintenance mode.
        if(exhibition.getMode()==ExhibitionModes.OFFLINE) {
            String modeValue = exhibition.getCustomMessage().equals("") == false ? exhibition.getCustomMessage() : exhibition.getMode().getValue();
            ((Model) args[modelIndex]).addAttribute("modeValue", modeValue);
        } else {
            ((Model) args[modelIndex]).addAttribute("modeValue", exhibition.getMode().getValue());
        }
        return "maintenance";
    }
    
    // Method to get the space or module id's from the request body.
    public Map<String, String> getIds(Object[] args, MethodSignature signature) { 
        Map<String, String> res = new HashMap<>();
        for(int i=0; i <signature.getParameterTypes().length; ++i) {
            if(signature.getParameterTypes()[i].equals(String.class)) {
                if(((String) args[i]).length() > 2) {
                    String prefix = ((String) args[i]).substring(0,3);
                    if(prefix.equalsIgnoreCase(IdPrefix.SPACEID.getValue())) {
                        res.put(IdPrefix.SPACEID.getValue(), (String) args[i]);
                    }
                    if(prefix.equalsIgnoreCase(IdPrefix.MODULEID.getValue())) {
                        res.put(IdPrefix.MODULEID.getValue(), (String) args[i]);
                    }
                }
            }
        }
        return res;
    }
}
