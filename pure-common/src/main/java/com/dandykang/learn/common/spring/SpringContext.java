package com.dandykang.learn.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Map;

/**
 * Administrator create on 2017/12/28
 **/
public class SpringContext implements ApplicationContextAware {
    protected static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> cls){
        return context.getBeansOfType(cls);
    }

    public static ApplicationContext getContext(){
        return context;
    }

    public static Object getBean(String beanName){
        Object bean;
        try {
            bean = context.getBean(beanName);
        } catch (Exception e){
            return null;
        }
        return bean;
    }

    public static BeanDefinition getBeanDefinition(String beanName){
        return ((GenericApplicationContext)context).getBeanDefinition(beanName);
    }
}
