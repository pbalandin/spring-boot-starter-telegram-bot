package io.github.pbalandin.telegram.bot.postprocessor.update;

import io.github.pbalandin.telegram.bot.api.Type;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateResolver {

    Boolean isApplicable(Update update);

    Long getChatId(Update update);

    String getMessage(Update update);

    Type getMessageType(Update update);
}
