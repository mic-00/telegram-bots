package it.michelemasetto.telegram.bots;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class TelegramBot extends DefaultLongPollingUpdateConsumer {

    protected final String botToken;
}
