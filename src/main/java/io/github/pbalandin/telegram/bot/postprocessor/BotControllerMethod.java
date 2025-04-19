package io.github.pbalandin.telegram.bot.postprocessor;

import io.github.pbalandin.telegram.bot.api.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@AllArgsConstructor
public class BotControllerMethod {
    private final @NonNull String command;
    private final @NonNull Type type;
    private final @NonNull Object bean;
    private final @NonNull Method method;
    private final String afterCommand;
}
