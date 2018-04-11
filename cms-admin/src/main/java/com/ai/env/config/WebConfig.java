package com.ai.env.config;

import java.util.List;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Servlet配置
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(WebConfig.class);

	@Bean
	public ViewResolver viewResolver() {
		logger.info("-------------------- ViewResolver init ---------------------");
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/errors/bad-request").setViewName(
				"errors/400");
		registry.addViewController("/errors/unauthorized").setViewName(
				"errors/401");
		registry.addViewController("/errors/not-found").setViewName(
				"errors/404");
		registry.addViewController("/errors/internal-server-error")
				.setViewName("errors/500");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations(
				"/assets/");
		registry.addResourceHandler("/static/**").addResourceLocations(
				"/static/");
		registry.addResourceHandler("/swagger/**").addResourceLocations(
				"/swagger/");
		registry.addResourceHandler("/upload/**").addResourceLocations(
				"/upload/");
		registry.addResourceHandler("/download/**").addResourceLocations(
				"/download/");
		registry.addResourceHandler("/video/**")
				.addResourceLocations("/video/");
		registry.addResourceHandler("/image/**")
				.addResourceLocations("/image/");
		registry.addResourceHandler("/xml/**").addResourceLocations("/xml/");
	}

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		logger.info("-------------------- DefaultAdvisorAutoProxyCreator init ---------------------");
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		logger.info("-------------------- AuthorizationAttributeSourceAdvisor init ---------------------");
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(org.apache.shiro.SecurityUtils
				.getSecurityManager());
		return aasa;
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		logger.info("-------------------- extendMessageConverters init ---------------------");
		for (@SuppressWarnings("rawtypes")
		HttpMessageConverter converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				((MappingJackson2HttpMessageConverter) converter)
						.getObjectMapper()
						.setDateFormat(new CustomDateFormat());
			}
		}
	}
}
