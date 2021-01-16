package com.xm.cpsmall.annotation;

import java.lang.annotation.*;

/**
 * 获取当前登录的用户信息
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pid {

    /**
     * 默认从request header中取出userId
     * @return
     */
    boolean necessary() default true;
}
