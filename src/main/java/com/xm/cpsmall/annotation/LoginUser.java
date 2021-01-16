package com.xm.cpsmall.annotation;

import java.lang.annotation.*;

/**
 * 获取当前登录的用户信息
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {

    /**
     * 是否必须存在
     * @return
     */
    boolean necessary() default true;

    /**
     * 是否获取最新用户信息
     * @return
     */
    boolean latest() default false;
}
