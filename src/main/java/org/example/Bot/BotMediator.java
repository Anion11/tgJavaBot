package org.example.Bot;
import org.example.DB.DB;
import org.example.entity.AppUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BotMediator extends TelegramLongPollingBot {
    final private DB db = new DB();
    Storage storage;
    public BotMediator() {
        storage = new Storage();
    }

    @Override
    public String getBotUsername() {
        return "javaTgSubscribe_bot";
    }

    @Override
    public String getBotToken() {
        return "7189595273:AAEL_fOhdd9xY31YnFwYakR0mTNAfT6PQ5E";
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
                String chatId = inMess.getChatId().toString();

                String[] responses = parseMessage(inMess);

                SendMessage outMess = new SendMessage();
                outMess.setChatId(chatId);
                for (String response: responses) {
                    outMess.setText(response);
                    execute(outMess);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private boolean checkValidKey(String key) {
        return Arrays.asList(storage.getKeys()).contains(key);
    }
    private boolean subscribeUser(Long userId) {
        return db.subscribeUser(userId);
    }
    private String UserInfo(Long UserId) {
        if (db.getUserData(UserId).getActive()) return "Подписка активна";
        else return "Подписка на данный момент не активна";
    }
    private void registerUser(Message message) {
        if (db.getUserData(message.getFrom().getId()) != null) {
            return;
        }
        AppUser user = new AppUser();
        user.setActive(false);
        user.setTelegramUserId(message.getFrom().getId());
        user.setLastName(message.getFrom().getLastName());
        user.setUsername(message.getFrom().getUserName());
        user.setFirstName(message.getFrom().getFirstName());
        db.createUser(user);
    }
    private boolean unSubscribeUser(Long userId) {
        return db.unSubscribeUser(userId);
    }
    private String[] parseMessage(Message message) {
        String[] response;
        String msg = message.getText().toLowerCase();
        if (storage.getCommands().containsKey(msg)) {
            response = storage.getCommands().get(msg);
            switch (msg) {
                case "/start":
                    registerUser(message);
                    break;
                case "/info":
                    response = new String[]{String.join(", ", storage.getCommands().get(message.getText())), UserInfo(message.getFrom().getId())};
                    break;
                default:
                    storage.getUsersLastAnswers().put(message.getChatId(), msg);
            }
        } else if (Objects.equals(storage.getUsersLastAnswers().get(message.getChatId()), "/subscribe")) {
            if (checkValidKey(msg)) {
                if (db.getUserData(message.getFrom().getId()).getActive()) {
                    response = new String[]{"У вас уже есть подписка!"};
                } else {
                    if (subscribeUser(message.getFrom().getId())) response =  new String[]{"Поздравляю! Вы получили подписку!"};
                    else response = new String[]{"Ошибка! Если такое повторится еще раз то напишите в поддержку"};
                }
            } else response =  new String[]{"Данный ключ не действителен или не существует!"};
        } else if (Objects.equals(storage.getUsersLastAnswers().get(message.getChatId()), "/unsubscribe")) {
            if (Objects.equals(msg, "да")) {
                if (unSubscribeUser(message.getFrom().getId())) response = new String[]{"Подписка удалена("};
                else response = new String[]{"Подписка не удалена (Ошибка)"};
            } else if (Objects.equals(msg, "нет")) response =  new String[]{"Отлично! Давайте продолжим!"};
            else response = new String[]{"Сообщение не распознано. Повторите попытку"};
        } else response = new String[]{"Сообщение не распознано. Повторите попытку"};

        return response;
    }
}