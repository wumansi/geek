package com.jdbc.jdbcpool.muldatasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class HandlerDataSourceAop {
    @Pointcut("@within(com.jdbc.jdbcpool.muldatasource.DynamicRoutingDataSource)||@annotation(com.jdbc.jdbcpool.muldatasource.DynamicRoutingDataSource)")
    public void pointcut(){}

    @Before(value = "pointcut()")
    public void beforeOpt(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getMethods();
        DynamicRoutingDataSource annotation = null;
        for (Method method : methods){
            if (joinPoint.getSignature().getName().equals(method.getName())){
                annotation = method.getAnnotation(DynamicRoutingDataSource.class);
                if (annotation == null){
                    annotation = joinPoint.getTarget().getClass().getAnnotation(DynamicRoutingDataSource.class);
                    if (annotation == null){
                        return;
                    }
                }
            }
        }
        String dataSourceName = annotation.value();
        MultiDataSource.setDataSourceKey(dataSourceName);
        System.out.println("切到" +  dataSourceName + "数据库");
    }

    @After(value = "pointcut()")
    public void afterOpt(){
        MultiDataSource.toDefault();
        System.out.println("切回默认数据库");
    }
}
