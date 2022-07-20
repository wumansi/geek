package com.demo.cache.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程并发计数.
 */
public class Counter {
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private int i = 0;
    //并发计算
    public static void main(String[] args){
        final Counter counter = new Counter();
        List<Thread> ts = new ArrayList<Thread>(600);
        long start = System.currentTimeMillis();
        for (int j = 0;j<100;j++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0;i<10000;i++){
                        counter.count();
                        counter.safeCount();
                    }
                }
            });
            ts.add(t);
        }
        for (Thread t : ts){
            t.start();
        }
        for (Thread t :ts){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(counter.i);
        System.out.println(counter.atomicInteger.get());
        System.out.println(System.currentTimeMillis() - start);
    }
    private void safeCount(){
        for (;;){
            int i = atomicInteger.get();
            boolean suc = atomicInteger.compareAndSet(i, ++i);
            if (suc){
                break;
            }
        }
    }
    private void count(){
        i++;
    }
}
