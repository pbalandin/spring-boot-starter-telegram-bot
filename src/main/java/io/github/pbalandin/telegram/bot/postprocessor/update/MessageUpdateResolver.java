package io.github.pbalandin.telegram.bot.postprocessor.update;

import io.github.pbalandin.telegram.bot.api.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.util.List;

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
    public Chat getChat(Update update) {
        return update.getMessage().getChat();
    }

    @Override
    public Message getMessage(Update update) {
        return update.getMessage();
    }

    @Override
    public String getText(Update update) {
        return StringUtils.defaultString(update.getMessage().getText());
    }

    @Override
    public List<PhotoSize> getPhotos(Update update) {
        return update.getMessage().getPhoto();
    }

    @Override
    public Video getVideo(Update update) {
        return update.getMessage().getVideo();
    }

    @Override
    public Animation getAnimation(Update update) {
        return update.getMessage().getAnimation();
    }

    @Override
    public Voice getVoice(Update update) {
        return update.getMessage().getVoice();
    }

    @Override
    public VideoNote getVideoNote(Update update) {
        return update.getMessage().getVideoNote();
    }

    @Override
    public Contact getContact(Update update) {
        return update.getMessage().getContact();
    }

    @Override
    public Location getLocation(Update update) {
        return update.getMessage().getLocation();
    }

    @Override
    public Poll getPoll(Update update) {
        return update.getMessage().getPoll();
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
