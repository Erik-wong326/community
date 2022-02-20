package com.cqupt.community.util;

import com.cqupt.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 工具类 HostHolder
 * HostHolder 起到一个容器的作用
 * 持有用户信息,用于代替session对象.
 *
 * 这里可以参考ThreadLocal源码是如何储存值的
 *
 *     public void set(T value) {
 *         Thread t = Thread.currentThread(); //获取当前线程
 *         ThreadLocal.ThreadLocalMap map = this.getMap(t); //get当前线程对应的map
 *         //由于每个线程的 map 不同,从而完成了线程隔离的目的
 *         if (map != null) {
 *             map.set(this, value);
 *         } else {
 *             this.createMap(t, value);
 *         }
 *     }
 *
 * //get方法的线程隔离方式也相同
 *      public T get() {
 *         Thread t = Thread.currentThread();
 *         ThreadLocal.ThreadLocalMap map = this.getMap(t);
 *         if (map != null) {
 *             ThreadLocal.ThreadLocalMap.Entry e = map.getEntry(this);
 *             if (e != null) {
 *                 T result = e.value;
 *                 return result;
 *             }
 *         }
 *
 *         return this.setInitialValue();
 *     }
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/19 18:18
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
