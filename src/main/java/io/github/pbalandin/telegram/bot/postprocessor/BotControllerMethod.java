package io.github.pbalandin.telegram.bot.postprocessor;

import io.github.pbalandin.telegram.bot.api.Type;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

@Getter
@Setter
public class BotControllerMethod {
    private final @NonNull String command;
    private final @NonNull Type type;
    private final @NonNull Object bean;
    private final Class<?> beanType;
    private final @NonNull Method method;
    private final MethodParameter[] methodParameters;
    private final String afterCommand;

    public BotControllerMethod(@NonNull String command, @NonNull Type type, @NonNull Object bean, @NonNull Method method, String afterCommand) {
        this.command = command;
        this.type = type;
        this.bean = bean;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = method;
        this.methodParameters = initMethodParameters();
        this.afterCommand = afterCommand;
    }

    private MethodParameter[] initMethodParameters() {
        int count = this.method.getParameterCount();
        MethodParameter[] result = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            MethodParameter parameter = new SynthesizingMethodParameter(this.method, i);
            GenericTypeResolver.resolveParameterType(parameter, this.beanType);
            result[i] = parameter;
        }
        return result;
    }
}
