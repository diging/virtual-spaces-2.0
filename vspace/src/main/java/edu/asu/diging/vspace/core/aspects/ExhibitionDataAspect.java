package edu.asu.diging.vspace.core.aspects;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Component
@Aspect
public class ExhibitionDataAspect {

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISpaceManager spaceManager;

    @After("execution(public * edu.asu.diging.vspace.web..*Controller.*(..))")
    public void setExhibition(JoinPoint jp) {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Class<?> returnType = signature.getReturnType();
        // only if there is a model object injected and if we're returning a string
        // (assuming that returning a string implies rendering of a view afterwards
        if (args != null && returnType == String.class) {
            for (Object obj : args) {
                if (obj instanceof Model && !(obj instanceof RedirectAttributes)) {
                    if (!((Model) obj).containsAttribute("exhibition")) {
                        ((Model) obj).addAttribute("exhibition", exhibitionManager.getStartExhibition());
                    }
                    if (!((Model) obj).containsAttribute("allSpaces")) {
                        ((Model) obj).addAttribute("allSpaces", spaceManager.getAllSpaces());
                    }
                }
            }
        }
    }
    
    @Around("execution(public * edu.asu.diging.vspace.web..ExhibitionSpaceController.*(..))")
    public Object showExhibition(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        AuthenticationFacade authFacade = new AuthenticationFacade();
        if (exhibition.getMode().equals(ExhibitionModes.ACTIVE.getValue())) {
            return jp.proceed();
        }
        else if(!(authFacade.getAuthenticatedUser() == null)){
           ((Model) args[1]).addAttribute("showModal","true");
            return jp.proceed();
        }
        
        //Public portal, no user logged in and exhibit is down, show just the maintenance/offline message.
        // return <create a new page>
        return "";
    }
}
