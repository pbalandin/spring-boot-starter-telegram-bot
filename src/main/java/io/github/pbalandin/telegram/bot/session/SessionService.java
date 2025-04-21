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
    private final Map<Long, TelegramSession> userSessionMap = new ConcurrentHashMap<>();

    public TelegramSession getSession(@NonNull Long id) {
        return userSessionMap.computeIfAbsent(id, k -> create(id));
    }

    public TelegramSession update(@NonNull Long id, @NonNull String method) {
        TelegramSession updatedSession = new TelegramSession(id, method);
        userSessionMap.put(id, updatedSession);
        return updatedSession;
    }

    private TelegramSession create(@NonNull Long id) {
        return new TelegramSession(id, StringUtils.EMPTY);
    }
}
