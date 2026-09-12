package it.michelemasetto.telegram.bots.helloworld;

import it.michelemasetto.telegram.bots.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelloWorldBot extends TelegramBot {

    private static final String HELLO_WORLD_BOT_TOKEN = "HELLO_WORLD_BOT_TOKEN";

    public HelloWorldBot() {
        super(System.getenv(HELLO_WORLD_BOT_TOKEN));
    }

    @Override
    protected void doConsume(Update update) throws Exception {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = SendMessage.builder().chatId(chatId).text("Hello World!").build();

            getTelegramClient().execute(sendMessage);
        }
    }
}
