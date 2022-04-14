package com.mansi.base.demo;

/**
 * 饿汉式（线程安全）.
 */
public class SingPatterm {
    private final static SingPatterm INSTANCE = new SingPatterm();

    public static SingPatterm getInstance(){
        return INSTANCE;
    }

}
