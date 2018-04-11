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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.ai.epg.**.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class MasterTransactionConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(MasterTransactionConfig.class);

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("masterDataSource")
	private DataSource masterDataSource;

	@Bean(name = "entityManager")
	@Primary
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- MasterEntityManager init ---------------------");
		EntityManager entityManager = entityManagerFactory(builder).getObject()
				.createEntityManager();
		return entityManager;
	}

	@Bean(name = "entityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- MasterEntityManagerFactory init ---------------------");
		logger.info("-------------------- MasterEntityManagerFactory masterDataSource = "
				+ masterDataSource);
		return builder.dataSource(masterDataSource)
				.properties(getVendorProperties(masterDataSource))
				.packages("com.ai.epg.**.entity").build();
	}

	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(
			EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- MasterTransactionManager init ---------------------");
		return new JpaTransactionManager(entityManagerFactory(builder)
				.getObject());
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

}
