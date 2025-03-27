package io.github.pbalandin.telegram.bot.bpp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@AllArgsConstructor
public class BotControllerMethod {
    private final Object bean;
    private final Method method;
}
