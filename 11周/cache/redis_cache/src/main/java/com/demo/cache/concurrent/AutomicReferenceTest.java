package com.demo.cache.concurrent;

import com.demo.cache.entity.User;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS修改多个共享变量.
 */
public class AutomicReferenceTest {

    public static void main(String[] args){
        User a = new User(1, "a");
        User b = new User(2, "b");

        AtomicReference<User> atomicUser = new AtomicReference<User>();
        atomicUser.set(a);
        for (int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean re = atomicUser.compareAndSet(a, b);
                    if (re){
                        System.out.println(Thread.currentThread().getId() + "修改成功");
                        System.out.println(Thread.currentThread().getId() + ",修改后值："  + atomicUser.get().getId() + "," +atomicUser.get().getUsername());
                    } else {
                        System.out.println(Thread.currentThread().getId() + "修改失败");
                    }
                }
            }).start();
        }
    }
}
