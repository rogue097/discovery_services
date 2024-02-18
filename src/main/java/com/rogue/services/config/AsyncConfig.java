package com.rogue.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1); // Minimum number of threads to keep alive
    executor.setMaxPoolSize(20); // Maximum number of threads
    executor.setQueueCapacity(50); // Queue size used for holding tasks before they are executed
    executor.setThreadNamePrefix("Async-Executor-");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30); // Wait time in seconds for tasks to complete on shutdown
    executor.initialize();
    return executor;
  }
}

