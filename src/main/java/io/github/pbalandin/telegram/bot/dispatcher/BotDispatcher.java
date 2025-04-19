package io.github.pbalandin.telegram.bot.dispatcher;

import io.github.pbalandin.telegram.bot.Bot;
import io.github.pbalandin.telegram.bot.api.BotResponse;
import io.github.pbalandin.telegram.bot.api.Type;
import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethod;
import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethodContainer;
import io.github.pbalandin.telegram.bot.postprocessor.update.UpdateResolver;
import io.github.pbalandin.telegram.bot.session.SessionService;
import io.github.pbalandin.telegram.bot.session.UserSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BotDispatcher implements Dispatcher {

    private final BotControllerMethodContainer container;
    private final Bot bot;
    private final SessionService sessionService;
    private final List<UpdateResolver> updateResolvers;


    public BotDispatcher(BotControllerMethodContainer container, Bot bot, SessionService sessionService, List<UpdateResolver> updateResolvers) {
        this.container = container;
        this.bot = bot;
        this.sessionService = sessionService;
        this.updateResolvers = updateResolvers;
    }

    @Override
    public void dispatch(Update update) {
        UpdateResolver resolver = updateResolvers.stream()
                .filter(it -> it.isApplicable(update))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No suitable update resolver found"));

        processCommand(update, resolver);
    }

    @SneakyThrows
    private void processCommand(Update update, UpdateResolver updateResolver) {
        Long chatId = updateResolver.getChatId(update);
        String command = updateResolver.getMessage(update);
        Type type = updateResolver.getMessageType(update);
        UserSession session = sessionService.getSession(chatId);

        Optional<BotControllerMethod> methodOpt = container.getMethod(command, type, session.getLastCommand());
        if (methodOpt.isPresent()) {
            BotControllerMethod method = methodOpt.get();
            Object result = method.getMethod().invoke(method.getBean(), update);

            if (result instanceof BotResponse<?> response) {
                sendResponse(response, chatId.toString());
            } else if (result == null) {

            } else {
                throw new UnsupportedOperationException("Unsupported response type: " + result.getClass());
            }

            sessionService.update(chatId, method.getMethod().getName());
        }
    }


    //TODO add other types of responses
    @SneakyThrows
    private void sendResponse(BotResponse<?> response, String chatId) {
        var method = response.body();
        if (method instanceof SendMessage sendMessages) {
            sendMessages.setChatId(chatId);
            bot.execute(sendMessages);
        } else if (method instanceof SendPhoto sendPhoto) {
            sendPhoto.setChatId(chatId);
            bot.execute(sendPhoto);
        } else if (method instanceof SendVideo sendVideo) {
            sendVideo.setChatId(chatId);
            bot.execute(sendVideo);
        } else if (method instanceof SendAudio sendAudio) {
            sendAudio.setChatId(chatId);
            bot.execute(sendAudio);
        } else if (method instanceof SendAnimation sendAnimation) {
            sendAnimation.setChatId(chatId);
            bot.execute(sendAnimation);
        } else if (method instanceof SendDocument sendDocument) {
            sendDocument.setChatId(chatId);
            bot.execute(sendDocument);
        } else if (method instanceof SendVoice sendVoice) {
            sendVoice.setChatId(chatId);
            bot.execute(sendVoice);
        } else if (method instanceof SendSticker sendSticker) {
            sendSticker.setChatId(chatId);
            bot.execute(sendSticker);
        } else {
            throw new UnsupportedOperationException("Unsupported partial method type: " + method.getClass().getName());
        }
    }
}
