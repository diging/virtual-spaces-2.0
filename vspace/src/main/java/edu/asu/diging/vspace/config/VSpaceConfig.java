package edu.asu.diging.vspace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"edu.asu.diging.vspace", "edu.asu.diging.simpleusers"})
@EnableWebMvc
@PropertySource({"classpath:config.properties", "${appConfigFile:classpath:}/app.properties"})
public class VSpaceConfig {

    /**
     * Spring 6 removed the commons-fileupload based resolver. Multipart parsing is
     * now done by the servlet container; upload size limits are configured via
     * the multipart-config element of the dispatcher servlet in web.xml.
     */
    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}
