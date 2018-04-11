package com.ai.env.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ai.common.cache.CacheKeyGenerator;

@Component
public class CacheManagerConfig extends CachingConfigurerSupport implements
		CommandLineRunner {

	private static final Logger logger = LoggerFactory
			.getLogger(CacheManagerConfig.class);

	private final CacheManager cacheManager;

	@Autowired
	public CacheManagerConfig(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public void run(String... strings) throws Exception {
		logger.info("\n\n"
				+ "=========================================================\n"
				+ "Using cache manager: "
				+ cacheManager.getClass().getName()
				+ "\n"
				+ "=========================================================\n\n");
	}

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new CacheKeyGenerator();
	}
}
