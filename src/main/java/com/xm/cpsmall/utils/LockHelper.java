package com.xm.cpsmall.utils;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * Mysql 设置悲观锁工具
 * 单次有效
 * 原理类似 OrderByHelper
 */
@Component
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class LockHelper implements Interceptor {
    private static final ThreadLocal<String> ORDERBY_LOCAL = new ThreadLocal<String>();
    private static Field additionalParametersField;

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 上锁
     */
    public static void lock() {
        ORDERBY_LOCAL.set("lock");
    }

    /**
     * 手动清空
     */
    public static void clear(){
        ORDERBY_LOCAL.remove();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String orderBy = ORDERBY_LOCAL.get();
        if(orderBy != null && orderBy.length() > 0){
            ORDERBY_LOCAL.remove();
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            //由于逻辑关系，只会进入一次
            if(args.length == 4){
                //4 个参数时
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                //6 个参数时
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
            String sql = boundSql.getSql();
            String orderBySql = sql + " for update ";
            //更新cacheKey，防止缓存错误#3
            cacheKey.update(orderBy);
            BoundSql orderbyBoundSql = new BoundSql(ms.getConfiguration(), orderBySql, boundSql.getParameterMappings(), parameter);
            Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
            for (String key : additionalParameters.keySet()) {
                orderbyBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
            }
            return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, orderbyBoundSql);
        } else {
            return invocation.proceed();
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
