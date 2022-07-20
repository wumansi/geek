package com.sisi.rpccore.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProviderService {
    // 接口名称
    String service();
    // 分组
    String group() default "default";
    String version() default "default";
    /**
     * tags:用于简单路由
     * 多个标签使用逗号分隔
     * @return tags
     */
    String tags() default "";
    int weight() default 1;
}
