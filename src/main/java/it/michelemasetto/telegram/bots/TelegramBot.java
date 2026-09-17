package it.michelemasetto.telegram.bots;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Getter
public abstract class TelegramBot extends DefaultLongPollingUpdateConsumer {

    private final String botToken;

    private final TelegramClient telegramClient;

    protected TelegramBot(String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    protected abstract void doConsume(Update update) throws Exception;

    @Override
    public final void consume(Update update) {
        try {
            doConsume(update);
        } catch (Exception e) {
            log.error("An error occurred during consumption", e);
        }
    }
}
