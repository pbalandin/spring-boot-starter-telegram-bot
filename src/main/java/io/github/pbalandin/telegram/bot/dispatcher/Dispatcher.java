package io.github.pbalandin.telegram.bot.dispatcher;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Dispatcher {

    void dispatch(Update update);
}
