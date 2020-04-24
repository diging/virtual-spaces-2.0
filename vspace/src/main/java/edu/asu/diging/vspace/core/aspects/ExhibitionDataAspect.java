package edu.asu.diging.vspace.core.aspects;

import java.util.Arrays;

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
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Component
@Aspect
public class ExhibitionDataAspect {

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISpaceManager spaceManager;

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
        int indexofSpaceId = (Arrays.asList(signature.getParameterTypes())).indexOf(String.class);
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        ISpace space = spaceManager.getSpace(args[indexofSpaceId].toString());
        if(exhibition==null) {
            return "home";
        }
        if(exhibition.getMode().equals(ExhibitionModes.ACTIVE)) {
            if(space!=null) {
                return jp.proceed();
            } else {
                return "notFound";
            }
        }
        if(authFacade.getAuthenticatedUser()!=null && indexofModel>-1) {
            if(space!=null) {
                ((Model) args[indexofModel]).addAttribute("showModal", "true");
                return jp.proceed();
            } else {
                return "notFound";
            }
        }
        if(exhibition.getMode()==ExhibitionModes.OFFLINE && !exhibition.getCustomMessage().equals("")) {
            ((Model) args[indexofModel]).addAttribute("modeValue", exhibition.getCustomMessage());
        } else {
            ((Model) args[indexofModel]).addAttribute("modeValue", exhibition.getMode().getValue());
        }
        return "maintenance";
    }
}
