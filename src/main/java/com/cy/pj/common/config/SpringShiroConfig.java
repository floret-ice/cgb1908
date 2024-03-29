package com.cy.pj.common.config;

import java.util.LinkedHashMap;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration //bean
public class SpringShiroConfig {
	@Bean   
	public DefaultWebSessionManager sessionManager() {
			 DefaultWebSessionManager sManager=
					 new DefaultWebSessionManager();
			 sManager.setGlobalSessionTimeout(60*60*1000);
			 return sManager;
		 }
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager=
				new CookieRememberMeManager();
		SimpleCookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(10*60);
		cManager.setCookie(cookie);
		return cManager;
	}
	@Bean
	public CacheManager shiroCacheManager(){
		 return new MemoryConstrainedCacheManager();
	}
	/**
     * @Bean 注解描述的方法，表示方法的返回值
                * 对象要交给Spring管理，默认key为方法名
     * SecurityManager对象是Shiro框架的核心
                * 注意：此对象为org.apache.shiro.mgt
                * 包中的对象。
     * @return
     */
	@Bean  //<bean id="" class=""/>
	public SecurityManager securityManager(
			Realm realm,
			CacheManager cacheManager,
			CookieRememberMeManager rememberManager,
			DefaultWebSessionManager sessionManager) {
		DefaultWebSecurityManager sManager = 
				new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setCacheManager(cacheManager);
		sManager.setRememberMeManager(rememberManager);
		sManager.setSessionManager(sessionManager);
		return sManager;		
	}
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactory(
			@Autowired SecurityManager securityManager) {
		ShiroFilterFactoryBean sfBean = 
				new ShiroFilterFactoryBean();
		sfBean.setSecurityManager(securityManager);
		sfBean.setLoginUrl("/doLoginUI");
		//定义map指定请求过滤规则(哪些资源允许匿名访问,哪些必须认证访问)
		LinkedHashMap<String, String> map = 
				new LinkedHashMap<>();
		//静态资源允许匿名访问:"anon"
		map.put("/bower_components/**","anon");
		map.put("/build/**","anon");
		map.put("/dist/**","anon");
		map.put("/plugins/**","anon");
		map.put("/user/doLogin","anon");
		map.put("/doLogout","logout");
		//除了匿名访问的资源,其它都要认证("authc")后访问
		map.put("/**","user"); //authc
		sfBean.setFilterChainDefinitionMap(map);
		return sfBean;		
	}
		
	//授权配置
	  /**初始化Advisor对象，此对象关联了一个切入点,例如你方法上
	      *是否有@RequiresPermissions注解*/
	@Bean
	public AuthorizationAttributeSourceAdvisor 
	newAuthorizationAttributeSourceAdvisor(
		    		    @Autowired SecurityManager securityManager) {
			        AuthorizationAttributeSourceAdvisor advisor=
					new AuthorizationAttributeSourceAdvisor();
	advisor.setSecurityManager(securityManager);
		return advisor;
	}
	
	
	

}
