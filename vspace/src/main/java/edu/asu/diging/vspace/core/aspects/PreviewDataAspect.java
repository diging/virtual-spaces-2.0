package edu.asu.diging.vspace.core.aspects;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.exhibit.view.ExhibitionConstants;

/**
 * 
 * This aspect intercepts the controller for the public site. It performs checks and adds information needed 
 * for the preview site if the preview site is requested.
 * @author prachikharge
 *
 */
@Component
@Aspect
public class PreviewDataAspect {

    @Autowired
    private IExhibitionManager exhibitionManager;

    /**
     * This method does the following: 
     * 
     * 1. Checks if the requested url is for the preview (if it contains a preview id)
     * 2. Checks if the preview id is correct (if not showing a 404)
     * 3. Adds the preview id into the model and sets isExhPreview to true
     * 
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("execution(public * edu.asu.diging.vspace.web.exhibit..*Controller.*(..)) || execution(public * edu.asu.diging.vspace.web.HomeController.*(..))")
    public Object checkPreview(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        int indexOfModel = (Arrays.asList(signature.getParameterTypes())).indexOf(Model.class);
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String previewId = (String) pathVariables.get(ExhibitionConstants.PREVIEW_ID);
        if (exhibition != null && previewId != null && exhibition.getPreviewId() != null) {
            if (!exhibition.getPreviewId().equals(previewId)) {
                return "redirect:/exhibit/404";
            }
            if (indexOfModel >= 0) {
                ((Model) args[indexOfModel]).addAttribute("isExhPreview", true);
                ((Model) args[indexOfModel]).addAttribute(ExhibitionConstants.PREVIEW_ID, previewId);
            }
        }
        return jp.proceed();
    }
}
