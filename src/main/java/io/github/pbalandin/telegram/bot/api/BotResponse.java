package io.github.pbalandin.telegram.bot.api;

import lombok.Builder;
import lombok.NonNull;


@Builder
public record BotResponse<T>(@NonNull T body) {

}