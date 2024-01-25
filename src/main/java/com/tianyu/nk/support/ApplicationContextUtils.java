package com.tianyu.nk.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * spring 上下文
 *
 * @author tianyuguo@yeah.net
 * @date 2024/1/25 10:31
 */
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class cls) {
        return (T) applicationContext.getBean(cls);
    }

    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

}
