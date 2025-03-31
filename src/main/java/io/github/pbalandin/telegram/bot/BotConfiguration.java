package io.github.pbalandin.telegram.bot;

import io.github.pbalandin.telegram.bot.dispatcher.Dispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(BotConfigurationProperties.class)
@ComponentScan(basePackageClasses = {BotConfiguration.class})
public class BotConfiguration {

    @Bean
    public Bot bot(BotConfigurationProperties properties, @Lazy Dispatcher dispatcher) {
        return new Bot(properties.username(), properties.token(), dispatcher);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        log.info("Registered Telegram bot: {}", bot.getBotUsername());
        return api;
    }
}
