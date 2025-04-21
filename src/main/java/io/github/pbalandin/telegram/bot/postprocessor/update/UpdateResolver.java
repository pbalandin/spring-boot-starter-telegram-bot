package io.github.pbalandin.telegram.bot.postprocessor.update;

import io.github.pbalandin.telegram.bot.api.Type;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.List;

public interface UpdateResolver {

    Boolean isApplicable(Update update);

    Long getChatId(Update update);

    Chat getChat(Update update);

    Message getMessage(Update update);

    String getText(Update update);

    List<PhotoSize> getPhotos(Update update);

    Video getVideo(Update update);

    Animation getAnimation(Update update);

    Voice getVoice(Update update);

    VideoNote getVideoNote(Update update);

    Contact getContact(Update update);

    Location getLocation(Update update);

    Poll getPoll(Update update);

    Type getMessageType(Update update);
}
