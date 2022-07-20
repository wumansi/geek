package com.mansi.base.demo.concurrent;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    private volatile Integer value = null;
    final Semaphore semaphore = new Semaphore(1);

    public void sum(int num) throws InterruptedException {
        semaphore.acquire();
        value = fibo(num);
        semaphore.release();
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() throws InterruptedException {
        int result;
        semaphore.acquire();
        result = this.value;
        semaphore.release();
        return result;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        final SemaphoreTest method = new SemaphoreTest();
        Thread thread = new Thread(() -> {
            try {
                method.sum(45);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        int result = method.getValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

}