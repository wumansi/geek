package com.sisi.rpccore.filter;

public class Retry {
    private static int retryLimit = 0;

    public static int getRetryLimit(){
        return retryLimit;
    }

    public static void setRetryLimit(int retryLimit){
        Retry.retryLimit = retryLimit;
    }
}
