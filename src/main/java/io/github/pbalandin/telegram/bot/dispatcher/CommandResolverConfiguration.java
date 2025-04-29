package io.github.pbalandin.telegram.bot.dispatcher;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandResolverConfiguration {

    @Bean
    @ConditionalOnMissingBean(CommandResolver.class)
    public CommandResolver defaultCommandResolver() {
        return new DefaultCommandResolver();
    }
}
