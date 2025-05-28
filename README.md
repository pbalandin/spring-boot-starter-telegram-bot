# Spring Boot Starter for Telegram Bot

[![Maven Central](https://img.shields.io/maven-central/v/io.github.pbalandin/spring-boot-starter-telegram-bot.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.pbalandin/spring-boot-starter-telegram-bot)

Spring Boot Starter for [TelegramBots](https://github.com/rubenlagus/TelegramBots) library.
Provides a convenient way to map bot commands using a controller-like experience.

- [Installation](#installation)
    - [Maven](#maven)
    - [Gradle](#gradle)
- [Quick Start](#quick-start)
- [Examples](#examples)
- [Argument Resolving](#argument-resolving)
- [Command Order and Flow](#command-order-and-flow)
- [Arguments](#arguments)
- [License](#license)

## Installation

### Maven

```xml

<dependency>
    <groupId>io.github.pbalandin</groupId>
    <artifactId>spring-boot-starter-telegram-bot</artifactId>
    <version>0.0.3</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.pbalandin:spring-boot-starter-telegram-bot:0.0.3'
```

## Quick Start

Add your bot token and username to the `application.properties`.

```properties
telegram.bot.token=<Your Bot Token>
telegram.bot.username=<Your Bot Username>
```

Create a handler class for your bot commands.

```java

@Handler
public class GreetingHandler {

    @Command("/hello")
    public BotResponse<SendMessage> hello(Message message) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Hello from bot!")
                .build();

        return new BotResponse<>(sendMessage);
    }
}
```

That's it! Now you can run your Spring Boot application.

## Examples

You can find example of a Telegram bot using this starter in
the [Telegram task bot](https://github.com/pbalandin/telegram-taskbot)

## Argument Resolving

Argument in the command handler methods are resolved automatically based on the method signature. See below of supported
argument types.
You can specify argument that should be extracted from the message text using regex patterns in the `@Command`
annotation.

```java

@Command("/register (\\w+)")
public BotResponse<SendMessage> register(Message message, @CommandParam(1) String username) {
    SendMessage sendMessage = SendMessage
            .builder()
            .chatId(message.getChatId())
            .text("Hello " + username + "! You have been registered successfully.")
            .build();

    return new BotResponse<>(sendMessage);
}
```

## Command Order and Flow

You can control the sequence in which commands are handled by using the `after` attribute in the `@Command` annotation.
This allows you to specify that a command handler should only be executed after another handler method has run.

```java

@Command("/support")
public BotResponse<SendMessage> support(Message message) {
    SendMessage sendMessage = SendMessage
            .builder()
            .chatId(message.getChatId())
            .text("Write your question and it will be delivered to the support.")
            .build();

    return new BotResponse<>(sendMessage);
}

@Command(value = "(.+)", after = "support")
public BotResponse<SendMessage> question(Message message, @CommandParam(1) String question) {

    // Handling the question

    SendMessage sendMessage = SendMessage
            .builder()
            .chatId(message.getChatId())
            .text("Your question has been received and will be answered soon")
            .build();

    return new BotResponse<>(sendMessage);
}
```

In this example, the question method will only be triggered after the support method has been executed.
**Note**: In the after attribute of the @Command annotation, you must specify the handler method name, not the command
value.

If you need to control the order in which command handlers are executed, use the `order` attribute in the `@Command`
annotation.

```java

@Command(value = ".+", order = -1)
public BotResponse<SendMessage> unrecognized(Message message) {
    SendMessage sendMessage = SendMessage
            .builder()
            .chatId(message.getChatId())
            .text("Unrecognized command. Please enter a valid command.")
            .build();

    return new BotResponse<>(sendMessage);
}
```

This handler will be executed only if no other command matches, because its negative order value ensures it runs last.

## Arguments

Method arguments can be used to pass additional parameters to the command handler.

Arguments extracted from the message text using regex patterns in the @Command annotation

| Type      | Description                               |
|-----------|-------------------------------------------|
| `String`  | Extracted as String from message text     |
| `Long`    | Extracted as a Long from message text     |
| `Integer` | Extracted as an Integer from message text |
| `Boolean` | Extracted as Boolean from message text    |

Arguments that you get from the Telegram message object

| Type                                                         | Description                                                         |
|--------------------------------------------------------------|---------------------------------------------------------------------|
| `org.telegram.telegrambots.meta.api.objects.Message`         | The full message object received from Telegram with all its content |
| `org.telegram.telegrambots.meta.api.objects.Chat`            | The chat where the message was sent                                 |
| `org.telegram.telegrambots.meta.api.objects.PhotoSize`       | A single photo size object from the message (if present)            |
| `List<org.telegram.telegrambots.meta.api.objects.PhotoSize>` | List of all photo sizes attached to the message (if present)        |
| `org.telegram.telegrambots.meta.api.objects.Video`           | The video object attached to the message (if present)               |
| `org.telegram.telegrambots.meta.api.objects.Voice`           | The voice message object attached to the message (if present)       |
| `org.telegram.telegrambots.meta.api.objects.VideoNote`       | The video note object attached to the message (if present)          |
| `org.telegram.telegrambots.meta.api.objects.Contact`         | The contact information attached to the message (if present)        |
| `org.telegram.telegrambots.meta.api.objects.Location`        | The location information attached to the message (if present)       |
| `org.telegram.telegrambots.meta.api.objects.polls.Poll`      | The poll object attached to the message (if present)                |
| `org.telegram.telegrambots.meta.api.objects.games.Animation` | The animation (GIF) object attached to the message (if present)     |

Telegram bot starter arguments:

| Type                                                       | Description                                                                       |
|------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `io.github.pbalandin.telegram.bot.session.TelegramSession` | Provides access to the current user's session data and state for the Telegram bot |

## License
```
MIT License

Copyright (c) 2025 Pavlo Balandin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```