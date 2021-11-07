package com.example.demo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义类加载器.
 */
public class HelloTestClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            Object hello = new HelloTestClassLoader().findClass("Hello").newInstance();
            System.out.println("类名：" + hello.getClass().getName());
            for (Method m : hello.getClass().getDeclaredMethods()){
                System.out.println("方法：" + m.getName());
                m.invoke(hello);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载Class.
     * @param name 类名
     * @return Class对象
     */
    @Override
    protected Class<?> findClass(String name){
        // 加载Hello.xlass文件
        String path = System.getProperty("user.dir");
        path = path + "/src/main/resources/Hello.xlass";
        System.out.println(path);
        File file = new File(path);
        int n;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] cls = new byte[bos.toByteArray().length];
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            while ((n = fis.read(bytes)) != -1){
                bos.write(bytes, 0 ,n);
            }
            byte b = (byte)255;
            cls = bos.toByteArray();
            // 处理xlass文件（反码）
            for (int i = 0; i < cls.length; i++) {
                cls[i] = (byte)(255-cls[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fis);
            close(bos);
        }
        return defineClass("Hello", cls, 0, cls.length);
    }


    private static void close(Closeable c) {
        if (c != null){
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
