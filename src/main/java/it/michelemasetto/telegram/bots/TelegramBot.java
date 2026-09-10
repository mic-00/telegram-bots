package it.michelemasetto.telegram.bots;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Getter
public abstract class TelegramBot extends DefaultLongPollingUpdateConsumer {

    protected final String botToken;

    protected final TelegramClient telegramClient;

    public TelegramBot(String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    public final void registerTo(TelegramBotsLongPollingApplication app) {
        try {
            app.registerBot(botToken, this);
        } catch (TelegramApiException e) {
            log.error("An error occurred during bot registration", e);
        }
    }
}
