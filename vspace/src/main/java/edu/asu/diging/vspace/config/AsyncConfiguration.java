package edu.asu.diging.vspace.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@PropertySource("classpath:config.properties")
public class AsyncConfiguration implements AsyncConfigurer  {

    @Value("${async_core_pool_size}")
    private int corePoolSize;
    
    @Value("${async_max_pool_size}")
    private int maxPoolSize;
    
    @Value("${async_queue_capacity}")
    private int queueCapacity;
    
    
    
    @Bean(name="asyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        
        System.out.println("size = "+corePoolSize);
        
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
        
        asyncExecutor.setCorePoolSize(corePoolSize);
        asyncExecutor.setMaxPoolSize(maxPoolSize);
        asyncExecutor.setQueueCapacity(queueCapacity);
        
        asyncExecutor.setThreadNamePrefix("threadAsync");
        asyncExecutor.setWaitForTasksToCompleteOnShutdown(true);
        asyncExecutor.initialize();
        
        return asyncExecutor;
    }
    
}