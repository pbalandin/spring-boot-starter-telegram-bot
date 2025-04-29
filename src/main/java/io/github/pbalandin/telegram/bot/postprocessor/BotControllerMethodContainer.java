package io.github.pbalandin.telegram.bot.postprocessor;

import io.github.pbalandin.telegram.bot.api.Type;
import io.github.pbalandin.telegram.bot.api.annotation.Command;
import io.github.pbalandin.telegram.bot.dispatcher.CommandResolver;
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

    private final CommandResolver commandResolver;

    public BotControllerMethodContainer(CommandResolver commandResolver) {
        this.commandResolver = commandResolver;
    }

    //TODO Check if possible to create separate beans for this methods
    public BotControllerMethod registerControllerMethod(Object bean, Method method) {
        Command mapping = method.getAnnotation(Command.class);
        String command = commandResolver.resolveCommand(mapping.value());
        BotControllerMethod controllerMethod = new BotControllerMethod(command, mapping.type(), bean, method, mapping.after());
        methods.put(method.getName(), controllerMethod);
        return controllerMethod;
    }

    public Optional<BotControllerMethod> getMethod(String command, Type type, String previousName) {
        return methods.entrySet().stream()
                .filter(entry -> command.matches(entry.getValue().getCommand()) && entry.getValue().getType().equals(type))
                .filter(entry -> entry.getValue().getAfterCommand().isEmpty() || previousName.matches(entry.getValue().getAfterCommand()))
                .findFirst()
                .map(Map.Entry::getValue);
    }
}
