package com.mansi.base.demo;

/**
 * 枚举单例模式（避免反射、序列化、反序列化问题）
 */
class EnumSingleton {
    private EnumSingleton(){
    }
    public static EnumSingleton getInstance(){
        return SingletonEnum.INSTANCE.getInstancee();
    }
    private static enum SingletonEnum{
        INSTANCE;
        private EnumSingleton singleton = null;
        SingletonEnum(){
            singleton = new EnumSingleton();
        }
        public EnumSingleton getInstancee(){
            return singleton;
        }

    }

}
