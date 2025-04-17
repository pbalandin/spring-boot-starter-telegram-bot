package io.github.pbalandin.telegram.bot.postprocessor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@AllArgsConstructor
public class BotControllerMethod {
    private final @NonNull Object bean;
    private final @NonNull Method method;
    private final String afterCommand;
}
