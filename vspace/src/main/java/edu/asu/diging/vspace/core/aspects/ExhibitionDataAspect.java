package edu.asu.diging.vspace.core.aspects;

import java.util.Arrays;
import java.util.HashMap;
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
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

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
    private ISpaceDisplayManager spaceDisplayManager;

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
                    if(!((Model) obj).containsAttribute("allSpaces")) {
                        ((Model) obj).addAttribute("allSpaces", spaceManager.getAllSpaces());
                    }
                }
            }  
        }
    }

    @Around("execution(public * edu.asu.diging.vspace.web.publicview..*Controller.*(..))")
    public Object showExhibition(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        int indexofModel = (Arrays.asList(signature.getParameterTypes())).indexOf(Model.class);
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition(); 
        if(exhibition==null) {
            return "home";
        }
        if(exhibition.getMode() == null) {
            return jp.proceed();
        }
        Map<String, String> ids = getIds(args, signature);
        String spaceId = ids.getOrDefault("spaceId", "");
        String moduleId = ids.getOrDefault("moduleId", "");
        if(spaceId.equals("") && moduleId.equals("") && 
            (exhibition.getMode().equals(ExhibitionModes.ACTIVE) || authFacade.getAuthenticatedUser()!=null)) {
            return "notFound";
        }
        return redirectRequest(jp, spaceId, moduleId, indexofModel, exhibition);
    }
    
    public Object redirectRequest(ProceedingJoinPoint jp, String spaceId, String moduleId, int modelIndex, Exhibition exhibition) throws Throwable{
        ISpace space = spaceManager.getSpace(spaceId);
        IModule module = moduleManager.getModule(moduleId);
        Object[] args = jp.getArgs();
        if(exhibition.getMode().equals(ExhibitionModes.ACTIVE)) {
            if(space==null && module==null) {
                return "notFound";
            }
            else {
                return jp.proceed();
            }
        }
        if(authFacade.getAuthenticatedUser()!=null && modelIndex>-1) {
            if(space==null && module==null) {
                return "notFound";
            }
            else {
                ((Model) args[modelIndex]).addAttribute("showModal", "true");
                return jp.proceed();
            }
        }
        if(exhibition.getMode()==ExhibitionModes.OFFLINE && !exhibition.getCustomMessage().equals("")) {
            ((Model) args[modelIndex]).addAttribute("modeValue", exhibition.getCustomMessage());
        } else {
            ((Model) args[modelIndex]).addAttribute("modeValue", exhibition.getMode().getValue());
        }
        return "maintenance";
    }
    
    public Map<String, String> getIds(Object[] args, MethodSignature signature) {
        Map<String, String> res = new HashMap<>();
        for(int i=0; i <signature.getParameterTypes().length; ++i) {
            if(signature.getParameterTypes()[i].equals(String.class)) {
                if(((String) args[i]).length() > 2) {
                    String prefix = ((String) args[i]).substring(0,3);
                    if(prefix.equalsIgnoreCase(IdPrefix.SPACEID.getValue())) {
                        res.put("spaceId", (String) args[i]);
                    }
                    else if(prefix.equalsIgnoreCase(IdPrefix.MODULEID.getValue())) {
                        res.put("moduleId", (String) args[i]);
                    }
                }
            }
        }
        return res;
    }
}
