package edu.asu.diging.vspace.web.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.vspace.core.exception.LanguageListConfigurationNotFoundException;
import edu.asu.diging.vspace.core.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SpaceNotFoundException;

@ControllerAdvice
public class ExhibitionExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExhibitionExceptionHandler.class);
    private static final String module_not_found="module_not_found";
    private static final String space_not_found="space_not_found";
    private static final String sequence_not_found="sequence_not_found";
    private static final String slide_not_found="slide_not_found";
    private static final String slide_not_found_in_sequence="slide_not_found_in_sequence";
    private static final String language_list_configuration_not_found="language_list_configuration_not_found";
    
    @ExceptionHandler({ ModuleNotFoundException.class })
    protected ModelAndView handleModuleNotFoundException(HttpServletRequest request, ModuleNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_code", module_not_found);
        modelAndView.addObject("showAlert", true);
        modelAndView.addObject("message", ex.getMessage());
        logger.info("ModuleNotFoundException Occured:: URL=" + request.getRequestURL());
        logger.info("Code:: "+module_not_found+" Message:: " + ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }
    
    @ExceptionHandler({ SlideNotFoundException.class })
    protected ModelAndView handleSlideNotFoundException(HttpServletRequest request, SlideNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_code", slide_not_found);
        modelAndView.addObject("showAlert", true);
        modelAndView.addObject("message", ex.getMessage());
        logger.info("SlideNotFoundException Occured:: URL=" + request.getRequestURL());
        logger.info("Code:: "+slide_not_found+" Message:: " + ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SequenceNotFoundException.class })
    protected ModelAndView handleSequenceNotFoundException(HttpServletRequest request, SequenceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_code", sequence_not_found);
        modelAndView.addObject("showAlert", true);
        modelAndView.addObject("message", ex.getMessage());
        logger.info("SequenceNotFoundException Occured:: URL=" + request.getRequestURL());
        logger.info("Code:: "+sequence_not_found+" Message:: " + ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SlidesInSequenceNotFoundException.class })
    protected ModelAndView handleSlidesInSequenceNotFoundException(HttpServletRequest request,
            SlidesInSequenceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_code", slide_not_found_in_sequence);
        modelAndView.addObject("showAlert", true);
        modelAndView.addObject("message", ex.getMessage());
        logger.info("SlidesInSequenceNotFoundException Occured:: URL=" + request.getRequestURL());
        logger.info("Code:: "+slide_not_found_in_sequence+" Message:: " + ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SpaceNotFoundException.class })
    protected ModelAndView handleSpaceNotFoundException(HttpServletRequest request,
            SlidesInSequenceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_code", space_not_found);
        modelAndView.addObject("showAlert", true);
        modelAndView.addObject("message", ex.getMessage());
        logger.info("SpaceNotFoundException Occured:: URL=" + request.getRequestURL());
        logger.info("Code:: "+space_not_found+" Message:: " + ex.getMessage());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }
    
    @ExceptionHandler({ LanguageListConfigurationNotFoundException.class })
    protected ModelAndView handleLanguageListConfigurationNotFoundException(HttpServletRequest request,
            LanguageListConfigurationNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error_code", language_list_configuration_not_found);
        modelAndView.addObject("showAlert", true);
        modelAndView.addObject("message", ex.getMessage());
        logger.error("LanguageListConfigurationNotFound Occured:: URL=" + request.getRequestURL());
        logger.error("Code:: "+language_list_configuration_not_found+" Cause:: " + ex);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

}