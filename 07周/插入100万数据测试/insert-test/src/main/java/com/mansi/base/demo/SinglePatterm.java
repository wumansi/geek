package com.mansi.base.demo;

/**
 * 懒汉式（非线程安全）.
 */
public class SinglePatterm {
    private static SinglePatterm INSTANCE;
    public static SinglePatterm getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SinglePatterm();
        }
        return INSTANCE;
    }
}
