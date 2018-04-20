package com.ai.env.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.ai.cms.**.repository", entityManagerFactoryRef = "slaveEntityManagerFactory", transactionManagerRef = "slaveTransactionManager")
@EnableTransactionManagement
public class SlaveTransactionConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(SlaveTransactionConfig.class);

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("slaveDataSource")
	private DataSource slaveDataSource;

	@Bean(name = "slaveEntityManager")
	public EntityManager slaveEntityManager(EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- SlaveEntityManager init ---------------------");
		EntityManager entityManager = slaveEntityManagerFactory(builder)
				.getObject().createEntityManager();
		return entityManager;
	}

	@Bean(name = "slaveEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean slaveEntityManagerFactory(
			EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- SlaveEntityManagerFactory init ---------------------");
		logger.info("-------------------- SlaveEntityManagerFactory slaveDataSource = "
				+ slaveDataSource);
		return builder.dataSource(slaveDataSource)
				.properties(getVendorProperties(slaveDataSource))
				.packages("com.ai.cms.**.entity").build();
	}

	@Bean(name = "slaveTransactionManager")
	public PlatformTransactionManager slaveTransactionManager(
			EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- SlaveTransactionManager init ---------------------");
		return new JpaTransactionManager(slaveEntityManagerFactory(builder)
				.getObject());
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

}
