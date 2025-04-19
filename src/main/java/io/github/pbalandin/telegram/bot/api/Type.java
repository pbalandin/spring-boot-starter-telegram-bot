package io.github.pbalandin.telegram.bot.api;

public enum Type {
    TEXT("text"),
    PHOTO("photo"),
    VIDEO("video"),
    AUDIO("audio"),
    DOCUMENT("document"),
    STICKER("sticker"),
    VOICE("voice"),
    VIDEO_NOTE("video_note"),
    ANIMATION("animation"),
    CONTACT("contact"),
    LOCATION("location"),
    POLL("poll");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
