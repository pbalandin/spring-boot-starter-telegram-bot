package io.github.pbalandin.telegram.bot.postprocessor;

import io.github.pbalandin.telegram.bot.api.annotation.Handler;
import io.github.pbalandin.telegram.bot.api.annotation.Command;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class BotControllerBeanPostProcessor implements BeanPostProcessor {

    private final BotControllerMethodContainer container;

    public BotControllerBeanPostProcessor(BotControllerMethodContainer container) {
        this.container = container;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (AnnotationUtils.findAnnotation(targetClass, Handler.class) != null) {
            for (Method method : targetClass.getDeclaredMethods()) {
                Command mapping = AnnotationUtils.findAnnotation(method, Command.class);
                if (mapping != null) {
                    container.registerControllerMethod(bean, method);
                }
            }
        }
        return bean;
    }
}
