package com.example.concurent;

import java.util.concurrent.*;

public class homework01 {
    final static CountDownLatch latch = new CountDownLatch(1);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start=System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(new task(latch));
        latch.await();
        int result = future.get();
        System.out.println("计算结果为："+ result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    static class task implements Callable<Integer> {
        private CountDownLatch latch;
        public task(CountDownLatch latch){
            this.latch = latch;
        }
        @Override
        public Integer call() {
            int result = sum();
            System.out.println("执行子线程任务");
            latch.countDown();
            return result;
        }
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
