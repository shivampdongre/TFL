package com.trust.server.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

	@Bean
	public ExecutorService taskExecutor() {
		int cores = Runtime.getRuntime().availableProcessors();
		return new ThreadPoolExecutor(cores * 2, cores * 16, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20000),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

}
