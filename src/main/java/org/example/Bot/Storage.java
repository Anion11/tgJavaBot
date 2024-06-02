package org.example.Bot;

import org.example.DB.SubscribeKeyDAO.SubscribeKeyDAO;
import org.example.entity.SubscribeKey;

import java.util.HashMap;

public class Storage {
    final private HashMap<String, String[]> commands = new HashMap<>();
    private final HashMap<Long, String> usersLastAnswers = new HashMap<>();
    private SubscribeKeyDAO SubKeyDAO = new SubscribeKeyDAO();
    public Storage(){
        commands.put("/start", new String[]{"Привет, я бот для проверки подписок", "Введите /subscribe для подключения подписки"});
        commands.put("/unsubscribe", new String[]{"Вы точно уверены что хотите отписаться?"});
        commands.put("/subscribe", new String[]{"Введите ключ для получения подписки:"});
        commands.put("/info", new String[]{"Вот информация о вашей подписке:"});
        commands.put("/posts", new String[]{"Посты всем подписчикам отправленны!"});
    }

    public long getAdminId() {
        return 721116972;
    }

    public HashMap<Long, String> getUsersLastAnswers() {
        return usersLastAnswers;
    }
    public void putUsersLastAnswers(Long id, String mes) {
        usersLastAnswers.put(id, mes);
    }
    public HashMap<String, String[]> getCommands() {
        return commands;
    }

    public String[] getKeys() {
        SubscribeKey[] subKeys = SubKeyDAO.getAllSubscribeKey();
        String[] keys = new String[subKeys.length];

        for (int i = 0; i < subKeys.length; i++) {
            keys[i] = subKeys[i].getKey();
        }
        return keys;
    }
}
