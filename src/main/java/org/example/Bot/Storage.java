package org.example.Bot;

import org.example.DB.SubscribeKeyDAO.SubscribeKeyDAO;
import org.example.entity.SubscribeKey;

import java.util.HashMap;
/**
 * Класс, представляющий хранилище для бота с различными структурами данных.
 */
public class Storage {
    /**
     * Хранит команды бота в виде пар ключ-значение, где ключом является строка команды, а значением массив строк с описанием команды.
     */
    final private HashMap<String, String[]> commands = new HashMap<>();
    /**
     * Хранит ID пользователей в качестве ключей и их последние ответы в виде строк в качестве значений.
     */
    private final HashMap<Long, String> usersLastAnswers = new HashMap<>();
    /**
     * DAO для работы с ключами подписки.
     */
    private SubscribeKeyDAO SubKeyDAO = new SubscribeKeyDAO();
    /**
     * Конструктор класса Storage.
     */
    public Storage(){
        commands.put("/start", new String[]{"Привет, я бот для проверки подписок", "Введите /subscribe для подключения подписки"});
        commands.put("/unsubscribe", new String[]{"Вы точно уверены что хотите отписаться?"});
        commands.put("/subscribe", new String[]{"Введите ключ для получения подписки:"});
        commands.put("/info", new String[]{"Вот информация о вашей подписке:"});
        commands.put("/posts", new String[]{"Посты всем подписчикам отправленны!"});
    }
    /**
     * Получить ID администратора.
     * @return ID администратора в виде числа типа long.
     */
    public long getAdminId() {
        return 721116972;
    }
    /**
     * Получить последние ответоы пользователей.
     * @return HashMap с ID пользователей в качестве ключей и их последними ответами в качестве значений.
     */
    public HashMap<Long, String> getUsersLastAnswers() {
        return usersLastAnswers;
    }
    /**
     * Добавить последний ответ пользователя в хранилище.
     * @param id ID пользователя.
     * @param mes Последний ответ пользователя.
     */
    public void putUsersLastAnswers(Long id, String mes) {
        usersLastAnswers.put(id, mes);
    }
    /**
     * Получить команды и их описания.
     * @return HashMap с командами в качестве ключей и их описаниями в качестве значений.
     */
    public HashMap<String, String[]> getCommands() {
        return commands;
    }
    /**
     * Получить массив ключей для подписки.
     * @return Массив ключей подписки.
     */
    public String[] getKeys() {
        SubscribeKey[] subKeys = SubKeyDAO.getAllSubscribeKey();
        String[] keys = new String[subKeys.length];

        for (int i = 0; i < subKeys.length; i++) {
            keys[i] = subKeys[i].getKey();
        }
        return keys;
    }
}
