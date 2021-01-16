package com.xm.cpsmall.config;

import com.xm.cpsmall.comm.shiro.filter.ShiroUserFilter;
import com.xm.cpsmall.comm.shiro.realm.ManageRealm;
import com.xm.cpsmall.comm.shiro.realm.WechatRealm;
import com.xm.cpsmall.comm.shiro.session.TokenSessionManager;
import lombok.Data;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroConfig {
    //shiro session名称
    private String sessionIdName;
    //shiro session超时时间
    private Integer sessionTimeout;

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost()+":"+redisProperties.getPort());
        redisManager.setPassword(redisProperties.getPassword());
        redisManager.setTimeout((int)redisProperties.getTimeout().toMillis());
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setPrincipalIdFieldName("openId");
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * <p>
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    public SessionManager sessionManager(RedisCacheManager redisCacheManager, RedisSessionDAO redisSessionDAO,TokenSessionManager manager){
        manager.setCacheManager(redisCacheManager);
        manager.setSessionDAO(redisSessionDAO);
        manager.setGlobalSessionTimeout(sessionTimeout);
        return manager;
    }

    /**
     * 重写系统自带的Realm管理，主要针对多realm
     */
//    @Bean
//    public ModularRealmAuthenticator modularRealmAuthenticator(UserModularRealmAuthenticator userModularRealmAuthenticator) {
//        //设置realm认证通过策略
//        userModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//        return userModularRealmAuthenticator;
//    }

    @Bean
    public SecurityManager defaultWebSecurityManager(SessionManager sessionManager, WechatRealm wechatRealm, ManageRealm manageRealm){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setRealms(Arrays.asList(wechatRealm,manageRealm));
        return securityManager;
    }



    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加自定义filter
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc",new ShiroUserFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put("/api-user/user/**", "authc");
//        filterChainDefinitionMap.put("/api-mall/product/**", "authc");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


}