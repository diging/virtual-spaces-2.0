package edu.asu.diging.vspace.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
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
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("threadAsync");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        
        return executor;
    }
    
}
