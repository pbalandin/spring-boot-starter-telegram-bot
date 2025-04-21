package io.github.pbalandin.telegram.bot.dispatcher;

import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethod;
import io.github.pbalandin.telegram.bot.postprocessor.update.UpdateResolver;
import io.github.pbalandin.telegram.bot.session.SessionService;
import io.github.pbalandin.telegram.bot.session.TelegramSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class MethodArgumentResolver {

    private final SessionService sessionService;

    public MethodArgumentResolver(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Object[] resolveMethodArguments(BotControllerMethod method, Update update, UpdateResolver resolver) {
        Class<?>[] parameterTypes = method.getMethod().getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            if (Update.class.isAssignableFrom(paramType)) {
                args[i] = update;
            } else if (Message.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getMessage(update);
            } else if (Chat.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getChat(update);
            } else if (TelegramSession.class.isAssignableFrom(paramType)) {
                args[i] = sessionService.getSession(resolver.getChatId(update));
            } else if (String.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getText(update);
            } else if (Long.class.isAssignableFrom(paramType) || long.class.isAssignableFrom(paramType)) {
                args[i] = Long.valueOf(resolver.getText(update));
            } else if (PhotoSize.class.isAssignableFrom(paramType)) {
                List<PhotoSize> photos = resolver.getPhotos(update);
                args[i] = photos != null && !photos.isEmpty() ? photos.get(photos.size() - 1) : null;
            } else if (Video.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getVideo(update);
            } else if (Animation.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getAnimation(update);
            } else if (Voice.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getVoice(update);
            } else if (VideoNote.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getVideoNote(update);
            } else if (Contact.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getContact(update);
            } else if (Location.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getLocation(update);
            } else if (Poll.class.isAssignableFrom(paramType)) {
                args[i] = resolver.getPoll(update);
            } else if (List.class.isAssignableFrom(paramType)) {
                Type genericType = method.getMethod().getGenericParameterTypes()[i];
                if (genericType instanceof ParameterizedType parameterizedType) {
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeArguments.length > 0 && typeArguments[0].equals(PhotoSize.class)) {
                        args[i] = resolver.getPhotos(update);
                    } else {
                        String typeName = typeArguments.length > 0 ? typeArguments[0].getTypeName() : "unknown";
                        throw new IllegalArgumentException(String.format("Unsupported List parameter type: List<%s> for class: %s, method: %s", typeName, method.getMethod().getDeclaringClass().getName(), method.getMethod().getName()));
                    }
                }
            } else {
                throw new IllegalArgumentException(String.format("Unsupported parameter type: %s, for class: %s, method: %s", paramType.getName(), method.getMethod().getDeclaringClass().getName(), method.getMethod().getName()));
            }
        }
        return args;
    }
}
