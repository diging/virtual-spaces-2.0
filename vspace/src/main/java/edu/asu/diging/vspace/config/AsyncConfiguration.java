package edu.asu.diging.vspace.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableAsync
@Configuration
public class AsyncConfiguration {
    
    @Value("${async_core_pool_size}")
    private int corePoolSize;
    
    @Value("${async_max_pool_size}")
    private int maxPoolSize;
    
    @Value("${async_queue_capacity}")
    private int queueCapacity;
    
    @Bean(name="asyncExecutor")
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