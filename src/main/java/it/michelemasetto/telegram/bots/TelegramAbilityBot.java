package it.michelemasetto.telegram.bots;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Slf4j
@Getter
public abstract class TelegramAbilityBot extends AbilityBot {

    private static final long CREATOR_ID = 838621822;

    private final String botToken;

    protected TelegramAbilityBot(String botToken, String botUsername) {
        super(new OkHttpTelegramClient(botToken), botUsername);
        this.botToken = botToken;
    }

    @Override
    public long creatorId() {
        return CREATOR_ID;
    }
}
