package io.github.pbalandin.telegram.bot.postprocessor.update;

import io.github.pbalandin.telegram.bot.api.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUpdateResolver implements UpdateResolver {
    @Override
    public Boolean isApplicable(Update update) {
        return update.hasMessage();
    }

    @Override
    public Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    @Override
    public String getMessage(Update update) {
        return StringUtils.defaultString(update.getMessage().getText());
    }

    @Override
    public Type getMessageType(Update update) {
        var message = update.getMessage();
        if (message.hasPhoto()) {
            return Type.PHOTO;
        } else if (message.hasVideo()) {
            return Type.VIDEO;
        } else if (message.hasAudio()) {
            return Type.AUDIO;
        } else if (message.hasAnimation()) {
            return Type.ANIMATION;
        } else if (message.hasVoice()) {
            return Type.VOICE;
        } else if (message.hasVideoNote()) {
            return Type.VIDEO_NOTE;
        } else if (message.hasSticker()) {
            return Type.STICKER;
        } else if (message.hasDocument()) {
            return Type.DOCUMENT;
        } else if (message.hasContact()) {
            return Type.CONTACT;
        } else if (message.hasLocation()) {
            return Type.LOCATION;
        } else if (message.hasPoll()) {
            return Type.POLL;
        }
        return Type.TEXT;
    }

}
