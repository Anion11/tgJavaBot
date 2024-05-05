package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class BotMediator extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "7189595273:AAEL_fOhdd9xY31YnFwYakR0mTNAfT6PQ5E";

    final private String BOT_NAME = "javaTgSubscribe_bot";

    Storage storage;

    BotMediator() {
        storage = new Storage();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if(update.hasMessage() && update.getMessage().hasText())
            {
                Message inMess = update.getMessage();

                //Достаем из inMess id чата пользователя
                String chatId = inMess.getChatId().toString();

                //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                String response = parseMessage(inMess.getText());

                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage outMess = new SendMessage();



                //Добавляем в наше сообщение id чата а также наш ответ
                outMess.setChatId(chatId);

                outMess.setText(response);



                //Отправка в чат
                execute(outMess);

            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    public String parseMessage(String textMsg) {
        String response;
        response = storage.getMessages().getOrDefault(textMsg, "Сообщение не распознано");
        return response;
    }
}