package edu.asu.diging.vspace.web.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ ModuleNotFoundException.class })
    protected ModelAndView handleModuleNotFoundException(HttpServletRequest request, ModuleNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        logger.info("ModuleNotFoundException Occured:: URL=" + request.getRequestURL());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ ModuleNotConfiguredException.class })
    protected ModelAndView handleModuleNotConfiguredException(HttpServletRequest request,
            ModuleNotConfiguredException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        logger.info("ModuleNotConfiguredException Occured:: URL=" + request.getRequestURL());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SlideNotFoundException.class })
    protected ModelAndView handleSlideNotFoundException(HttpServletRequest request, SlideNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        logger.info("SlideNotFoundException Occured:: URL=" + request.getRequestURL());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SequenceNotFoundException.class })
    protected ModelAndView handleSequenceNotFoundException(HttpServletRequest request, SequenceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        logger.info("SequenceNotFoundException Occured:: URL=" + request.getRequestURL());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SlidesInSequenceNotFoundException.class })
    protected ModelAndView handleSlidesInSequenceNotFoundException(HttpServletRequest request,
            SlidesInSequenceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        logger.info("SlidesInSequenceNotFoundException Occured:: URL=" + request.getRequestURL());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

    @ExceptionHandler({ SpaceNotFoundException.class })
    protected ModelAndView handleSpaceNotFoundException(HttpServletRequest request,
            SlidesInSequenceNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        logger.info("SpaceNotFoundException Occured:: URL=" + request.getRequestURL());
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("module");
        return modelAndView;
    }

}