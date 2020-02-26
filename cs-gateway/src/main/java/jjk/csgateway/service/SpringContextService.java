package jjk.csgateway.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author: jjk
 * @create: 2020-01-06 16:34
 * @program: cloudStudy
 * @description: 动态获取spring上下文
 */
@Component
public class SpringContextService implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        SpringContextService.beanFactory=beanFactory;
    }


    public static <T> T getBean(String beanName){
        if(null!=beanFactory){
            T bean = (T)beanFactory.getBean(beanName);
            return bean;
        }
        return null;
    }
}
