package edu.asu.diging.vspace.core.aspects;

import java.lang.reflect.Method;

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

import java.lang.reflect.Parameter;

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
    
    @Around("execution(public * edu.asu.diging.vspace.web..Exhibition*Controller.*(..))")
    public void showExhibition(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        for(Object ob: args) {
           // System.out.println(":: " + ob);
        }
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] paras = method.getParameters();
        for(Parameter ob: paras) {
            //System.out.println("-- " + ob);
        }
        jp.proceed(args);
    }
    
}
