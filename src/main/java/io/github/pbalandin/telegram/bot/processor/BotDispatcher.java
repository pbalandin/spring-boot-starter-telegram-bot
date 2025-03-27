package io.github.pbalandin.telegram.bot.processor;

import io.github.pbalandin.telegram.bot.bpp.BotControllerMethod;
import io.github.pbalandin.telegram.bot.bpp.BotControllerMethodContainer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;


@Component
public class BotDispatcher implements Dispatcher {

    private final BotControllerMethodContainer container;

    public BotDispatcher(BotControllerMethodContainer container) {
        this.container = container;
    }

    @SneakyThrows
    @Override
    public void dispatch(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = extractCommand(update);
            Optional<BotControllerMethod> optional = container.getMethod(command);

            if (optional.isPresent()) {
                BotControllerMethod method = optional.get();
                method.getMethod().invoke(method.getBean(), update);
            }
        }
    }

    private static String extractCommand(Update update) {
        return update.getMessage().getText();
    }
}
