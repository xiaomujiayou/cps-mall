package com.xm.cpsmall.utils.lock;

import java.util.Optional;
import java.util.concurrent.locks.Lock;

/**
 * 锁的模板工具
 */
public class LockUtil {

    /**
     * 无返回值模板
     * @param lock
     * @param doWork
     */
    public static void lock(Lock lock,DoWork doWork){
        try {
            if(lock == null)
                throw new NullPointerException("lock 不存在");
            lock.lock();
            doWork.dowork();
        }finally {
            if(lock != null)
                lock.unlock();
        }
    }

    /**
     * 有返回值
     * @param lock
     * @param doWork
     * @param <T>
     * @return
     */
    public static <T> Optional<T> lock(Lock lock,DoWorkWithResult doWork) {
        Optional<T> optional = null;
        try {
            if (lock == null)
                throw new NullPointerException("lock 不存在");
            lock.lock();
            optional = Optional.of((T) doWork.dowork());
            return optional;
        }catch (Exception e){
            throw e;
        }finally {
            if(lock != null)
                lock.unlock();
        }
    }
}
