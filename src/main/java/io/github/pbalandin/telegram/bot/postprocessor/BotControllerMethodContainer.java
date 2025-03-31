package io.github.pbalandin.telegram.bot.postprocessor;

import io.github.pbalandin.telegram.bot.api.annotation.BotMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class BotControllerMethodContainer {

    private final Map<String, BotControllerMethod> methods = new HashMap<>();

    //TODO Check if possible to create separate beans for this methods
    public BotControllerMethod registerControllerMethod(Object bean, Method method) {
        BotControllerMethod controllerMethod = new BotControllerMethod(bean, method);
        BotMapping mapping = method.getAnnotation(BotMapping.class);
        methods.put(mapping.value(), controllerMethod);
        return new BotControllerMethod(bean, method);
    }

    public Optional<BotControllerMethod> getMethod(String command) {
        return Optional.ofNullable(methods.get(command));
    }
}
