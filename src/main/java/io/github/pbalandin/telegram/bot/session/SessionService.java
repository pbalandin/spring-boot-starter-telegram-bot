package io.github.pbalandin.telegram.bot.session;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SessionService {
    private final Map<Long, UserSession> userSessionMap = new ConcurrentHashMap<>();

    public UserSession getSession(@NonNull Long chatId) {
        return userSessionMap.computeIfAbsent(chatId, k -> create(chatId));
    }

    public UserSession update(@NonNull Long chatId, @NonNull String method) {
        UserSession updatedSession = new UserSession(chatId, method);
        userSessionMap.put(chatId, updatedSession);
        return updatedSession;
    }

    private UserSession create(@NonNull Long chatId) {
        return new UserSession(chatId, StringUtils.EMPTY);
    }
}
