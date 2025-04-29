package io.github.pbalandin.telegram.bot.dispatcher;

public class DefaultCommandResolver implements CommandResolver {
    @Override
    public String resolveCommand(String command) {
        return command;
    }
}
