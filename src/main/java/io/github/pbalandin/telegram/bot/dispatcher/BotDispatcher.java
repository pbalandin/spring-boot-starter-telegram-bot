package io.github.pbalandin.telegram.bot.dispatcher;

import io.github.pbalandin.telegram.bot.Bot;
import io.github.pbalandin.telegram.bot.api.BotResponse;
import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethod;
import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethodContainer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
@Component
public class BotDispatcher implements Dispatcher {

    private final BotControllerMethodContainer container;
    private final Bot bot;


    public BotDispatcher(BotControllerMethodContainer container, Bot bot) {
        this.container = container;
        this.bot = bot;
    }

    @Override
    public void dispatch(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String command = update.getMessage().getText();
                processCommand(command, update, update.getMessage().getChatId().toString());
            } else if (update.hasCallbackQuery()) {
                String command = update.getCallbackQuery().getData();
                processCommand(command, update, update.getCallbackQuery().getMessage().getChatId().toString());
            }
        } catch (Exception e) {
            log.error("Error dispatching update: {}", update);
            throw new RuntimeException("Error dispatching update", e);
        }
    }

    private void processCommand(String command, Update update, String chatId) throws ReflectiveOperationException {
        Optional<BotControllerMethod> methodOpt = container.getMethod(command);
        if (methodOpt.isPresent()) {
            BotControllerMethod method = methodOpt.get();
            Object result = method.getMethod().invoke(method.getBean(), update);

            if (result instanceof BotResponse<?> response) {
                sendResponse(response, chatId);
            } else {
                throw new UnsupportedOperationException("Unsupported response type: " + result.getClass());
            }

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
