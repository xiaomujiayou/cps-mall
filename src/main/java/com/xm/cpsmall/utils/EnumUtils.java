package com.xm.cpsmall.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumUtils {

    /**
     * 通过枚举某个属性获取其实例(这个属性应当是唯一的)
     * @param clzz
     * @param keyName
     * @param key
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T extends Enum> T getEnum (Class<T> clzz, String keyName,Object key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] objects = clzz.getEnumConstants();
        String methodName = "get" + keyName.substring(0,1).toUpperCase()+keyName.substring(1,keyName.length());
        Method coinAddressCode = clzz.getMethod(methodName);
        for (Object object : objects) {
            if(coinAddressCode.invoke(object).equals(key)){
                return (T)object;
            }
        }
        throw new EnumConstantNotPresentException(clzz,methodName  +" 找不到所对应的枚举" +" "+ key);
    }
}
