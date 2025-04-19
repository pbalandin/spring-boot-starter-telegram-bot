package io.github.pbalandin.telegram.bot.api.annotation;

import io.github.pbalandin.telegram.bot.api.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();

    String after() default "";

    Type type() default Type.TEXT;
}
