package io.github.pbalandin.telegram.bot.dispatcher;

public interface CommandResolver {

    String resolveCommand(String command);
}
