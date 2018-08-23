package edu.asu.diging.vspace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

@Configuration
@EnableAspectJAutoProxy
@PropertySource("classpath:/config.properties")
public class VSpaceConfig {
	
	@Value("${max_upload_size}")
	private String maxUploadSize;

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(new Long(maxUploadSize));
	    return multipartResolver;
	}
	
}
