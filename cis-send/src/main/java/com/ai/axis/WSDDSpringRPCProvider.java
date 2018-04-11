package com.ai.axis;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.deployment.wsdd.WSDDConstants;
import org.apache.axis.deployment.wsdd.WSDDProvider;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WSDDSpringRPCProvider extends WSDDProvider implements
		ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public Handler newProviderInstance(WSDDService service,
			EngineConfiguration registry) throws Exception {
		return new SpringRPCProvider(applicationContext);
	}

	@Override
	public String getName() {
		return "SpringRPC";
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		WSDDProvider.registerProvider(new QName(WSDDConstants.URI_WSDD_JAVA,
				"SpringRPC"), this);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
