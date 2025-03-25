package io.github.pbalandin.telegram.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
public record BotConfigurationProperties(
        String token,
        String username
) {
}
