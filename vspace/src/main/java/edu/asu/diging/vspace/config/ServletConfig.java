package edu.asu.diging.vspace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServletConfig implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    /**
     * Spring 6 no longer matches URLs with a trailing slash (e.g. "/staff/dashboard/")
     * to handlers mapped without one ("/staff/dashboard"). Keep the Spring 5 behavior
     * so existing bookmarks and links with a trailing slash keep working.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}
