package org.example;

import org.example.Bot.BotMediator;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws TelegramApiException, SQLException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        BotMediator bot = new BotMediator();

        botsApi.registerBot(bot);
    }
}