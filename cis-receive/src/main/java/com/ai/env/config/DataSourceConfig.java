package com.ai.env.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(DataSourceConfig.class);

	@Bean(name = "masterDataSource")
	@Qualifier("masterDataSource")
	@Primary
	@ConfigurationProperties(prefix = "datasource.epg")
	public DataSource masterDataSource() {
		logger.info("-------------------- masterDataSource init ---------------------");
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "slaveDataSource")
	@Qualifier("slaveDataSource")
	@ConfigurationProperties(prefix = "datasource.cms")
	public DataSource slaveDataSource() {
		logger.info("-------------------- slaveDataSource init ---------------------");
		return DataSourceBuilder.create().build();
	}

}
