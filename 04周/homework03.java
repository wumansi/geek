package com.example.concurent;

import java.util.concurrent.CompletableFuture;

public class homework03 {
    public static void main(String[] args){
        long start=System.currentTimeMillis();
        Integer result1 = CompletableFuture.supplyAsync(()->{
            int result = sum();
            return result;
        }).thenApply(v-> v).join();
        System.out.println("计算结果为："+ result1);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
