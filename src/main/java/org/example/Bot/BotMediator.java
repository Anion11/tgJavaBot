package org.example.Bot;
import org.example.DB.DB;
import org.example.entity.AppUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

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
                outMess.setReplyMarkup(setMenu());

                if (Objects.equals(storage.getUsersLastAnswers().get(inMess.getChatId()), "/unsubscribe")) outMess.setReplyMarkup(setButtons());

                for (String response: responses) {
                    outMess.setText(response);
                    execute(outMess);
                }
            } else if (update.hasCallbackQuery()) {
                String callbackData = update.getCallbackQuery().getData();
                int messageId = update.getCallbackQuery().getMessage().getMessageId();
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                String response;

                if (callbackData.equals("YES_BUTTON")) {
                    if (unSubscribeUser(update.getCallbackQuery().getFrom().getId())) response = "Подписка удалена(";
                    else response = "Подписка не удалена (Ошибка)";
                } else if (callbackData.equals("NO_BUTTON")) response =  "Отлично! Давайте продолжим!";
                else response = "Ошибка";

                EditMessageText message = new EditMessageText();
                message.setChatId(chatId);
                message.setMessageId(messageId);
                message.setText(response);

                execute(message);
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
    private boolean subscribeUser(Long userId, String key) {
        return db.subscribeUser(userId, key);
    }
    public ReplyKeyboardMarkup setMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        Set<String> commands = storage.getStingCommands();
        for(String key: commands) {
            if (!Objects.equals(key, "/start")) row.add(new KeyboardButton(key));
            if (row.size() >= 3) {
                list.add(row);
                row = new KeyboardRow();
            }
        }
        if (!row.isEmpty()) {
            list.add(row);
        }
        keyboardMarkup.setKeyboard(list);
        return keyboardMarkup;
    }
    public InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("ДА");
        button1.setCallbackData("YES_BUTTON");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("НЕТ");
        button2.setCallbackData("NO_BUTTON");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button1);
        row.add(button2);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }
    private String UserInfo(Long UserId) {
        if (db.getUserData(UserId).getActive()) return "Подписка активна";
        else return "Подписка на данный момент не активна";
    }
    private void registerUser(Message message) {
        if (db.getUserData(message.getFrom().getId()) != null) {
            System.out.println("[log] Пользователь "+ db.getUserData(message.getFrom().getId()) + " существует");
            return;
        }
        AppUser user = new AppUser();
        user.setActive(false);
        user.setTelegramUserId(message.getFrom().getId());
        user.setLastName(message.getFrom().getLastName());
        user.setUsername(message.getFrom().getUserName());
        user.setFirstName(message.getFrom().getFirstName());
        if (db.createUser(user)) System.out.println("[log] Пользователь " + user + " успешно создан");
        else System.out.println("[log] Пользователь не создан");
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
                case "/subscribe":
                    if (db.getUserData(message.getFrom().getId()).getActive()) response = new String[]{"У вас уже есть подписка!"};
                    break;
                case "/unsubscribe":
                    if (!db.getUserData(message.getFrom().getId()).getActive())  {
                        response = new String[]{"У вас еще нет подписки!"};
                        storage.getUsersLastAnswers().put(message.getChatId(), "");
                        return response;
                    }
                    break;
            }
            storage.getUsersLastAnswers().put(message.getChatId(), msg);
        } else if (Objects.equals(storage.getUsersLastAnswers().get(message.getChatId()), "/subscribe")) {
            if (checkValidKey(msg)) {
                if (subscribeUser(message.getFrom().getId(), msg)) response =  new String[]{"Поздравляю! Вы получили подписку!"};
                else response = new String[]{"Ошибка! Если такое повторится еще раз то напишите в поддержку"};
            } else response =  new String[]{"Данный ключ не действителен или не существует!"};
        } else response = new String[]{"Сообщение не распознано. Повторите попытку"};
        return response;
    }
}