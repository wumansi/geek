package com.mansi.base.demo;

/**
 * 懒加载（内部类）
 * 单例类加载时，并不会立即加载内部类
 * 访问内部类的属性时，才会触发类的加载，线程安全
 */
public class InnerClassSingleton {
    private static class SingletonHolder{
        private static InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }
    private InnerClassSingleton(){

    }
    public static InnerClassSingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
}
