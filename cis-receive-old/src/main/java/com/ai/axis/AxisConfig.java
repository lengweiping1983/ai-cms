package com.ai.axis;

import org.apache.axis.EngineConfigurationFactory;
import org.apache.axis.transport.http.AdminServlet;
import org.apache.axis.transport.http.AxisServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxisConfig {

	@Bean
	public ServletRegistrationBean axisServletRegistrationBean() {
		System.setProperty(EngineConfigurationFactory.SYSTEM_PROPERTY_NAME,
				com.ai.axis.EngineConfigurationFactory.class.getName());
		AxisServlet servlet = new AxisServlet();
		ServletRegistrationBean servletBean = new ServletRegistrationBean(
				servlet, "/services/*", "/axis-secured/*");
		return servletBean;
	}

	@Bean
	public ServletRegistrationBean axisAdminServletRegistrationBean() {
		ServletRegistrationBean servletBean = new ServletRegistrationBean(
				new AdminServlet(), "/axis-admin");
		servletBean.setLoadOnStartup(100);
		return servletBean;
	}
}
