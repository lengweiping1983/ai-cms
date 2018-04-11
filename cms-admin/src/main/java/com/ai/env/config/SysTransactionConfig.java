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
@EnableJpaRepositories(basePackages = "com.ai.sys.**.repository", entityManagerFactoryRef = "sysEntityManagerFactory", transactionManagerRef = "sysTransactionManager")
@EnableTransactionManagement
public class SysTransactionConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(SysTransactionConfig.class);

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("sysDataSource")
	private DataSource sysDataSource;

	@Bean(name = "sysEntityManager")
	public EntityManager sysEntityManager(EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- SysEntityManager init ---------------------");
		EntityManager entityManager = sysEntityManagerFactory(builder)
				.getObject().createEntityManager();
		return entityManager;
	}

	@Bean(name = "sysEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean sysEntityManagerFactory(
			EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- SysEntityManagerFactory init ---------------------");
		logger.info("-------------------- SysEntityManagerFactory sysDataSource = "
				+ sysDataSource);
		return builder.dataSource(sysDataSource)
				.properties(getVendorProperties(sysDataSource))
				.packages("com.ai.sys.**.entity").build();
	}

	@Bean(name = "sysTransactionManager")
	public PlatformTransactionManager sysTransactionManager(
			EntityManagerFactoryBuilder builder) {
		logger.info("-------------------- SysTransactionManager init ---------------------");
		return new JpaTransactionManager(sysEntityManagerFactory(builder)
				.getObject());
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

}
