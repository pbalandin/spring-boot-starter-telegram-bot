package io.github.pbalandin.telegram.bot.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSession {
    private @NonNull Long chatId;
    private @NonNull String lastCommand;
}