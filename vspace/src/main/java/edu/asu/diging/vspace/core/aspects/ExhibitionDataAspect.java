package edu.asu.diging.vspace.core.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.lang.reflect.Parameter;

import edu.asu.diging.simpleusers.core.model.impl.User;
import edu.asu.diging.vspace.core.auth.impl.AuthenticationFacade;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
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

        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] paras = method.getParameters();
        Object values = jp.proceed();
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        AuthenticationFacade authFacade = new AuthenticationFacade();
        if (exhibition.getMode().equals(ExhibitionModes.ACTIVE.getValue())) {
            return values;
        }
        else if(!(authFacade.getAuthenticatedUser() == null)){
            //Still show the exhibition but with pop up and message that exhibit is down.
            return values;
        }
        //Public portal, no user logged in and exhbit is down, show just the maintenance/offline message.
        // return a maintenance page ? create a new page?
        return "";
    }
}
