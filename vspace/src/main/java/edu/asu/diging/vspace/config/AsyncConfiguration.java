package edu.asu.diging.vspace.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;


@Configuration
public class AsyncConfiguration implements AsyncConfigurer  {

//    @Value("${async_core_pool_size}")
//    private int corePoolSize;
//    
//    @Value("${async_max_pool_size}")
//    private int maxPoolSize;
//    
//    @Value("${async_queue_capacity}")
//    private int queueCapacity;
//    
    
    @Bean(name="asyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        System.out.println("corePoolSize");
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("threadAsync");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        
        return executor;
    }
    
}
