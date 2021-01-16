package com.xm.cpsmall.module.mall.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.PlatformTypeConstant;
import com.xm.cpsmall.module.mall.constant.PlatformTypeEnum;
import com.xm.cpsmall.module.mall.serialize.ex.SmProductEntityEx;
import com.xm.cpsmall.module.mall.serialize.form.*;
import com.xm.cpsmall.module.mall.service.api.*;
import com.xm.cpsmall.utils.EnumUtils;
import com.xm.cpsmall.utils.form.BaseForm;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务调度程序
 *  代理不同平台的服务接口
 */
@Aspect
@Component
public class DispatchServiceAspect {

    /**
     * 默认服务
     * 其他服务接口出错或者返回null,则返回该服务数据
     */
    private static final int DEFAULT_PLATFORM = PlatformTypeConstant.PDD;

    @Lazy
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 注册的服务容器
     */
    private final Map<Class, Map<String,Object>> services = new HashMap<>();

    /**
     * 需要统一管理的服务
     */
    private final List<Class> needConstruct = CollUtil.newArrayList(
            BannerService.class,
            GoodsListService.class,
            GoodsService.class,
            OptGoodsListService.class,
            OptionService.class
    );

    /**
     * 初始化容器
     */
    @PostConstruct
    private void init(){
        needConstruct.stream().forEach(o ->{
            Map<String,Object> oneTypeServices = webApplicationContext.getBeansOfType(o);
            services.put(o,oneTypeServices);
        });
    }

    /**
     * 获取单个服务实例
     * @param baseForm
     * @param clz
     * @param <T>
     * @return
     */
    private  <T> T getService(BaseForm baseForm, Class<T> clz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        PlatformTypeEnum typeEnum = EnumUtils.getEnum(PlatformTypeEnum.class,"type",baseForm.getPlatformType());
        Class[] interfaces = clz.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if(services.containsKey(interfaces[i])) {
                return (T) services.get(interfaces[i]).get(typeEnum.getServicePrefix() + interfaces[i].getSimpleName());
            }
        }
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE,"找不到对应的服务！");
    }

    @Pointcut("execution(public * com.xm.cpsmall.module.mall.service.api.impl.def.*.*(*))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object goodsDetailMessagePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if(!(joinPoint.getArgs()[i] instanceof BaseForm))
                continue;
            BaseForm baseForm = (BaseForm)joinPoint.getArgs()[i];
            Object service = getService(baseForm,targetMethod.getDeclaringClass());
            Method method = ReflectUtil.getMethodByName(service.getClass(),targetMethod.getName());
            Object result = null;
            try {
                result = method.invoke(service,joinPoint.getArgs());
            }catch (InvocationTargetException e){
                throw e.getTargetException();
            }
            if(result == null){
                //调用默认服务接口
                baseForm.setPlatformType(DEFAULT_PLATFORM);
                service = getService(baseForm,targetMethod.getDeclaringClass());
                method = ReflectUtil.getMethodByName(service.getClass(),targetMethod.getName());
                result = method.invoke(service,joinPoint.getArgs());
            }
            return result;
        }
        throw new GlobleException(MsgEnum.SERVICE_UNKNOW);
    }

    /**
      * 获取商品列表
      * @param params
      * @param clzs
      * @return
      * @throws NoSuchMethodException
      * @throws IllegalAccessException
      * @throws InvocationTargetException
      */
    public PageBean<SmProductEntityEx> getList(JSONObject params, Class... clzs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String methodName = params.getString("listType");
        BaseForm baseForm = null;
        if("option".equals(methodName)){
            baseForm = params.toJavaObject(OptionGoodsListForm.class);
        }else if("keyword".equals(methodName)){
            baseForm = params.toJavaObject(KeywordGoodsListForm.class);
        }else if("theme".equals(methodName)) {
            baseForm = params.toJavaObject(ThemeGoodsListForm.class);
        }else if("similar".equals(methodName)){
            baseForm = params.toJavaObject(SimilarGoodsListForm.class);
        }else if("mall".equals(methodName)){
            baseForm = params.toJavaObject(MallGoodsListForm.class);
        }else {
            baseForm = params.toJavaObject(GoodsListForm.class);
        }
        return getList(baseForm,methodName,clzs);
    }
    private PageBean<SmProductEntityEx> getList(BaseForm baseForm,String methodName, Class... clzs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object[] services = getServices(baseForm.getPlatformType(),clzs);
        for (int i = 0; i < services.length; i++) {
            Method method = ReflectUtil.getMethodByName(services[i].getClass(),methodName);
            if(method != null){
                Object result = method.invoke(services[i], baseForm);
                if(result == null){
                    baseForm.setPlatformType(DEFAULT_PLATFORM);
                    return getList(baseForm,methodName,clzs);
                }
                return (PageBean<SmProductEntityEx>)result;
            }else if(i < services.length - 1) {
                continue;
            }else {
                throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,methodName + " 不存在！");
            }
        }
        throw new GlobleException(MsgEnum.UNKNOWN_ERROR);
    }

    /**
      * 获取多个服务实例
      * @param platformType
      * @param clzs
      * @return
      */
    private Object[] getServices(Integer platformType, Class... clzs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object[] result = new Object[clzs.length];
        for (int i = 0; i < clzs.length; i++) {
            PlatformTypeEnum typeEnum = EnumUtils.getEnum(PlatformTypeEnum.class,"type",platformType);
            result[i] = services.get(clzs[i]).get(typeEnum.getServicePrefix() + clzs[i].getSimpleName());
        }
        return result;
    }
}
