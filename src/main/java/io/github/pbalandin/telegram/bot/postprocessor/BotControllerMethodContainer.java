package io.github.pbalandin.telegram.bot.postprocessor;

import io.github.pbalandin.telegram.bot.api.annotation.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
public class BotControllerMethodContainer {

    private final Map<String, BotControllerMethod> methods = new HashMap<>();

    //TODO Check if possible to create separate beans for this methods
    public BotControllerMethod registerControllerMethod(Object bean, Method method) {
        Command mapping = method.getAnnotation(Command.class);
        BotControllerMethod controllerMethod = new BotControllerMethod(bean, method, mapping.after());
        methods.put(mapping.value(), controllerMethod);
        return controllerMethod;
    }

    public Optional<BotControllerMethod> getMethod(String command, String previousName) {
        return methods.entrySet().stream()
                .filter(entry -> command.matches(entry.getKey()))
                .filter(entry -> entry.getValue().getAfterCommand().isEmpty() || previousName.matches(entry.getValue().getAfterCommand()))
                .findFirst()
                .map(Map.Entry::getValue);
    }
}
