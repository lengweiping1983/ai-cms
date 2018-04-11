package com.ai.env.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.ai.sys.security.UserAuthorizingRealm;
import com.ai.sys.security.UserFilter;
import com.ai.sys.security.UserFormAuthenticationFilter;

/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean() {
        logger.info("-------------------- MethodInvokingFactoryBean init ---------------------");
        MethodInvokingFactoryBean mi = new MethodInvokingFactoryBean();
        DefaultWebSecurityManager[] os = {getDefaultWebSecurityManager()};
        mi.setArguments(os);
        mi.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        return mi;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        logger.info("-------------------- DefaultWebSecurityManager init ---------------------");
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(getAuthorizingRealm());
        defaultWebSecurityManager.setCacheManager(getEhCacheManager());
        return defaultWebSecurityManager;
    }

    @Bean(name = "realm")
    public AuthorizingRealm getAuthorizingRealm() {
        logger.info("-------------------- AuthorizingRealm init ---------------------");
        return new UserAuthorizingRealm();
    }

    @Bean(name = "shiroEhcacheManager")
    public MemoryConstrainedCacheManager getEhCacheManager() {
        logger.info("-------------------- EhCacheManager init ---------------------");
        MemoryConstrainedCacheManager em = new MemoryConstrainedCacheManager();
        return em;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        logger.info("-------------------- LifecycleBeanPostProcessor init ---------------------");
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        logger.info("-------------------- ShiroFilterFactoryBean init ---------------------");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/errors/unauthorized");

        shiroFilterFactoryBean.getFilters().put("user", new UserFilter());
        shiroFilterFactoryBean.getFilters().put("authc", new UserFormAuthenticationFilter());

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/favicon.ico**", "anon");
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/video/**", "anon");
        filterChainDefinitionMap.put("/image/**", "anon");
		filterChainDefinitionMap.put("/kaptcha/**", "anon");// 验证码

        filterChainDefinitionMap.put("/header", "anon");
        filterChainDefinitionMap.put("/leftMenu", "anon");
        filterChainDefinitionMap.put("/navigation", "anon");
        filterChainDefinitionMap.put("/footer", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");
        filterChainDefinitionMap.put("/errors/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/lock", "authc");

        filterChainDefinitionMap.put("/trace", "anon");
        filterChainDefinitionMap.put("/logfile", "anon");
        filterChainDefinitionMap.put("/dump", "anon");
        filterChainDefinitionMap.put("/mappings", "anon");
        filterChainDefinitionMap.put("/jolokia", "anon");

        filterChainDefinitionMap.put("/autoconfig", "anon");
        filterChainDefinitionMap.put("/env", "anon");
        filterChainDefinitionMap.put("/configprops", "anon");
        filterChainDefinitionMap.put("/health", "anon");
        filterChainDefinitionMap.put("/metrics", "anon");

        filterChainDefinitionMap.put("/beans", "anon");
        filterChainDefinitionMap.put("/info", "anon");

        filterChainDefinitionMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        logger.info("-------------------- FilterRegistrationBean init ---------------------");
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("shiroFilter");
        // delegatingFilterProxy.setTargetFilterLifecycle(true);
        filterRegistration.setFilter(delegatingFilterProxy);
        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

}