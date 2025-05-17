package io.github.pbalandin.telegram.bot.dispatcher;

import io.github.pbalandin.telegram.bot.Bot;
import io.github.pbalandin.telegram.bot.api.BotResponse;
import io.github.pbalandin.telegram.bot.api.Type;
import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethod;
import io.github.pbalandin.telegram.bot.postprocessor.BotControllerMethodContainer;
import io.github.pbalandin.telegram.bot.postprocessor.update.UpdateResolver;
import io.github.pbalandin.telegram.bot.session.SessionService;
import io.github.pbalandin.telegram.bot.session.TelegramSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
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
    private final MethodArgumentResolver methodArgumentResolver;
    private final CommandResolver commandResolver;


    public BotDispatcher(BotControllerMethodContainer container, Bot bot, SessionService sessionService, List<UpdateResolver> updateResolvers, MethodArgumentResolver methodArgumentResolver, CommandResolver commandResolver) {
        this.container = container;
        this.bot = bot;
        this.sessionService = sessionService;
        this.updateResolvers = updateResolvers;
        this.methodArgumentResolver = methodArgumentResolver;
        this.commandResolver = commandResolver;
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
        String rawCommand = updateResolver.getText(update);
        String command = commandResolver.resolveCommand(rawCommand);
        Type type = updateResolver.getMessageType(update);
        TelegramSession session = sessionService.getSession(chatId);

        Optional<BotControllerMethod> methodOpt = container.getMethod(command, type, session.getLastCommand());
        if (methodOpt.isPresent()) {
            BotControllerMethod method = methodOpt.get();
            Object[] args = methodArgumentResolver.resolveMethodArguments(method, update, updateResolver);
            Object result = method.getMethod().invoke(method.getBean(), args);

            if (result instanceof BotResponse<?> response) {
                sendResponse(response);
            } else if (result == null) {

            } else {
                throw new UnsupportedOperationException("Unsupported response type: " + result.getClass());
            }

            sessionService.update(chatId, method.getMethod().getName());
        }
    }

    @SneakyThrows
    private void sendResponse(BotResponse<?> response) {
        var method = response.body();
        if (method instanceof SendMessage sendMessages) {
            bot.execute(sendMessages);
        } else if (method instanceof SendPhoto sendPhoto) {
            bot.execute(sendPhoto);
        } else if (method instanceof SendVideo sendVideo) {
            bot.execute(sendVideo);
        } else if (method instanceof SendAudio sendAudio) {
            bot.execute(sendAudio);
        } else if (method instanceof SendAnimation sendAnimation) {
            bot.execute(sendAnimation);
        } else if (method instanceof SendDocument sendDocument) {
            bot.execute(sendDocument);
        } else if (method instanceof SendVoice sendVoice) {
            bot.execute(sendVoice);
        } else if (method instanceof SendSticker sendSticker) {
            bot.execute(sendSticker);
        } else if (method instanceof EditMessageText editMessageText) {
            bot.execute(editMessageText);
        } else if (method instanceof EditMessageReplyMarkup editMessageReplyMarkup) {
            bot.execute(editMessageReplyMarkup);
        } else if (method instanceof EditMessageLiveLocation editMessageLiveLocation) {
            bot.execute(editMessageLiveLocation);
        } else if (method instanceof EditMessageCaption editMessageCaption) {
            bot.execute(editMessageCaption);
        } else if (method instanceof EditMessageMedia editMessageMedia) {
            bot.execute(editMessageMedia);
        } else if (method instanceof DeleteMessage deleteMessage) {
            bot.execute(deleteMessage);
        } else if (method instanceof SendChatAction sendChatAction) {
            bot.execute(sendChatAction);
        } else if (method instanceof SendContact sendContact) {
            bot.execute(sendContact);
        } else if (method instanceof SendDice sendDice) {
            bot.execute(sendDice);
        } else if (method instanceof SendGame sendGame) {
            bot.execute(sendGame);
        } else if (method instanceof SendLocation sendLocation) {
            bot.execute(sendLocation);
        } else if (method instanceof SendMediaGroup sendMediaGroup) {
            bot.execute(sendMediaGroup);
        } else if (method instanceof SendVenue sendVenue) {
            bot.execute(sendVenue);
        } else if (method instanceof SendVideoNote sendVideoNote) {
            bot.execute(sendVideoNote);
        } else if (method instanceof SendPoll sendPoll) {
            bot.execute(sendPoll);
        } else if (method instanceof StopPoll stopPoll) {
            bot.execute(stopPoll);
        } else {
            throw new UnsupportedOperationException("Unsupported partial method type: " + method.getClass().getName());
        }
    }
}
