package edu.asu.diging.vspace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"edu.asu.diging.vspace.core.data", "edu.asu.diging.simpleusers.core.data"})
public class PersistanceConfig {

}
