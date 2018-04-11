package com.ai.env.config;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class BeanConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(BeanConfig.class);

	private static final String ERROR_MESSAGE_SOURCE = "ErrorMessages";
	private static final String VALIDATION_MESSAGE_SOURCE = "ValidationMessages";

	@Bean(name = "errorMessageSource")
	public MessageSource errorMessageSource() {
		logger.info("-------------------- ErrorMessageSource init ---------------------");
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(ERROR_MESSAGE_SOURCE);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		logger.info("-------------------- MessageSource init ---------------------");
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(VALIDATION_MESSAGE_SOURCE);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	@Bean(name = "validator")
	public LocalValidatorFactoryBean validator() {
		logger.info("-------------------- Validator init ---------------------");
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		return validator;
	}

	@Bean(name = "characterEncodingFilter")
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Bean(name = "slaveOpenEntityManagerInViewFilter")
	public Filter slaveOpenEntityManagerInViewFilter() {
		OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
		openEntityManagerInViewFilter
				.setEntityManagerFactoryBeanName("slaveEntityManagerFactory");
		return openEntityManagerInViewFilter;
	}

	@Bean(name = "sysOpenEntityManagerInViewFilter")
	public Filter sysOpenEntityManagerInViewFilter() {
		OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
		openEntityManagerInViewFilter
				.setEntityManagerFactoryBeanName("sysEntityManagerFactory");
		return openEntityManagerInViewFilter;
	}

	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				logger.info("-------------------- ConfigurableEmbeddedServletContainer init ---------------------");
				container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,
						"/400"));
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
						"/404"));
				container.addErrorPages(new ErrorPage(
						HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
			}
		};
	}
}
