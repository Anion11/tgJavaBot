package org.example.Bot;
import org.example.DB.AppUserDAO.AppUserDAO;
import org.example.DB.SubscribeDAO.SubscribeDAO;
import org.example.DB.SubscribePostDAO.SubscribePostDAO;
import org.example.entity.AppUser;
import org.example.entity.Subscribe;
import org.example.entity.SubscribePost;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.*;
/**
 * Класс BotMediator - посредник для работы с ботом Telegram.
 */
public class BotMediator extends TelegramLongPollingBot {
    /**
     * Объект для доступа к данным пользователей приложения.
     */
    AppUserDAO AppUserDAO = new AppUserDAO();
    /**
     * Объект для доступа к данным подписок пользователей.
     */
    SubscribeDAO SubscribeDAO = new SubscribeDAO();
    /**
     * Объект для доступа к данным постов подписок.
     */
    SubscribePostDAO SubscribePostDAO = new SubscribePostDAO();
    /**
     * Объект для управления хранилищем данных.
     */
    Storage storage;
    /**
     * Конструктор класса BotMediator, инициализирует объект хранилища Storage.
     */
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
    /**
     * Метод для обработки обновлений, получаемых от Telegram API.
     * @param update Обновление от Telegram API.
     */
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
                outMess.setReplyMarkup(setMenu(inMess.getFrom().getId()));

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
    /**
     * Метод для проверки допустимости ключа.
     * @param key Ключ для проверки.
     * @return true, если ключ допустим, в противном случае - false.
     */
    private boolean checkValidKey(String key) {
        String[] keys = storage.getKeys();
        for (String k : keys) {
            if (k.equals(key)) return true;
        }
        return false;
    }
    /**
     * Метод для подписки пользователя.
     * @param userId Идентификатор пользователя.
     * @param key Ключ подписки.
     * @return true, если подписка успешно оформлена, в противном случае - false.
     */
    private boolean subscribeUser(Long userId, String key) {
        Subscribe subscribe = SubscribeDAO.getSubscribe(key);
        return AppUserDAO.subscribeUser(userId, subscribe);
    }
    /**
     * Метод для отображения меню пользователю.
     * @param id Идентификатор пользователя.
     * @return ReplyKeyboardMarkup с кнопками меню.
     */
    public ReplyKeyboardMarkup setMenu(Long id) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        Set<String> commands = storage.getCommands().keySet();
        for(String key: commands) {
            if (!Objects.equals(key, "/start") && !Objects.equals(key, "/posts")) row.add(new KeyboardButton(key));
            if (row.size() >= 3) {
                list.add(row);
                row = new KeyboardRow();
            }
        }
        if (!row.isEmpty()) list.add(row);
        if (id == storage.getAdminId()) {
            row = new KeyboardRow();
            row.add(new KeyboardButton("/posts"));
            list.add(row);
        }
        keyboardMarkup.setKeyboard(list);
        return keyboardMarkup;
    }
    /**
     * Метод для создания кнопок "ДА" и "НЕТ" для инлайн клавиатуры.
     * @return InlineKeyboardMarkup с кнопками "ДА" и "НЕТ".
     */
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
    /**
     * Метод для получения информации о пользователе.
     * @param UserId Идентификатор пользователя.
     * @return Информация о пользователе и состоянии подписки.
     */
    private String UserInfo(Long UserId) {
        if (AppUserDAO.getUserInfo(UserId).getSubscribe().getActive()) return AppUserDAO.getUserInfo(UserId).getSubscribe().toString();
        else return "Подписка на данный момент не активна";
    }
    /**
     * Метод для регистрации нового пользователя.
     * @param message Сообщение пользователя.
     */
    private void registerUser(Message message) {
        if (AppUserDAO.getUserInfo(message.getFrom().getId()) != null) {
            System.out.println("[log] Пользователь "+ AppUserDAO.getUserInfo(message.getFrom().getId()) + " существует");
            return;
        }
        AppUser user = new AppUser();
        user.setSubscribe(null);
        user.setTelegramUserId(message.getFrom().getId());
        user.setLastName(message.getFrom().getLastName());
        user.setUsername(message.getFrom().getUserName());
        user.setFirstName(message.getFrom().getFirstName());
        if (AppUserDAO.createAppUser(user)) System.out.println("[log] Пользователь " + user + " успешно создан");
        else System.out.println("[log] Пользователь не создан");
    }
    /**
     * Метод для отписки пользователя.
     * @param userId Идентификатор пользователя.
     * @return true, если отписка прошла успешно, в противном случае - false.
     */
    private boolean unSubscribeUser(Long userId) {
        return AppUserDAO.unSubscribeUser(userId);
    }
    /**
     * Метод для отправки постов подписчикам.
     * @param users Список пользователей подписчиков.
     * @throws TelegramApiException
     */
    private void sendPosts(ArrayList<AppUser> users) throws TelegramApiException {
        for (AppUser user : users) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(user.getTelegramUserId()));
            SubscribePost post = SubscribePostDAO.getRandomSubscribePost(user.getSubscribe().getSubscribeType());
            InputStream inputStream = getClass().getResourceAsStream("/" + post.getSrc() + ".png");
            InputFile photoFile = new InputFile(inputStream, post.getSrc() + ".png");
            sendPhoto.setPhoto(photoFile);
            sendPhoto.setCaption(post.getDescr());
            execute(sendPhoto);
        }
    }
    /**
     * Метод для парсинга сообщения и выполнения соответствующих действий.
     * @param message Сообщение пользователя.
     * @return Массив строк с ответами на сообщение.
     * @throws TelegramApiException
     */
    private String[] parseMessage(Message message) throws TelegramApiException {
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
                    if (AppUserDAO.getUserInfo(message.getFrom().getId()).getSubscribe().getActive()) {
                        response = new String[]{"У вас уже есть подписка!"};
                    }
                    break;
                case "/unsubscribe":
                    if (!AppUserDAO.getUserInfo(message.getFrom().getId()).getSubscribe().getActive())  {
                        response = new String[]{"У вас еще нет подписки!"};
                        storage.putUsersLastAnswers(message.getChatId(), "");
                        return response;
                    }
                    break;
                case "/posts":
                    if (message.getFrom().getId() == storage.getAdminId()) {
                        sendPosts(AppUserDAO.getAllSubscribers());
                        response = storage.getCommands().get(message.getText());
                    }
                    break;
            }
            storage.putUsersLastAnswers(message.getChatId(), msg);
        } else if (Objects.equals(storage.getUsersLastAnswers().get(message.getChatId()), "/subscribe")) {
            if (checkValidKey(msg)) {
                if (subscribeUser(message.getFrom().getId(), msg)) response =  new String[]{"Поздравляю! Вы получили подписку!"};
                else response = new String[]{"Ошибка! Если такое повторится еще раз то напишите в поддержку"};
            } else response =  new String[]{"Данный ключ не действителен или не существует!"};
        } else response = new String[]{"Сообщение не распознано. Повторите попытку"};
        return response;
    }
}