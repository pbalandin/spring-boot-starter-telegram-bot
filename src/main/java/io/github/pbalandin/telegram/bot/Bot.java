package io.github.pbalandin.telegram.bot;

import io.github.pbalandin.telegram.bot.dispatcher.Dispatcher;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final String username;
    private final String token;

    private final Dispatcher dispatcher;

    public Bot(String username, String token, Dispatcher dispatcher) {
        this.username = username;
        this.token = token;
        this.dispatcher = dispatcher;
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.dispatch(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
