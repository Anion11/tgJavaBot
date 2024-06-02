package org.example;

import org.example.Bot.BotMediator;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
/**
 * Основной класс, который инициализирует и запускает Telegram бота.
 */
public class Main {
    /**
     * Основной метод, являющийся точкой входа в приложение бота.
     *
     * @throws TelegramApiException Если происходит ошибка при взаимодействии с Telegram API
     */
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        BotMediator bot = new BotMediator();

        botsApi.registerBot(bot);
    }
}