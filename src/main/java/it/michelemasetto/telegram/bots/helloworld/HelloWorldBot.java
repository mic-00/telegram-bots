package it.michelemasetto.telegram.bots.helloworld;

import it.michelemasetto.telegram.bots.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
public class HelloWorldBot extends TelegramBot {

    private static final String HELLO_WORLD_BOT_TOKEN = "HELLO_WORLD_BOT_TOKEN";

    private final TelegramClient telegramClient;

    public HelloWorldBot() {
        super(System.getenv(HELLO_WORLD_BOT_TOKEN));
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = SendMessage.builder().chatId(chatId).text("Hello World!").build();

            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("An error occurred during client execution", e);
            }
        }
    }
}
