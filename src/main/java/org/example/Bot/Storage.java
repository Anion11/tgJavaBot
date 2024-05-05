package org.example.Bot;

import org.example.DB.DB;
import java.util.HashMap;

public class Storage {
    final private HashMap<String, String[]> commands = new HashMap<>();

    public HashMap<Long, String> getUsersLastAnswers() {
        return usersLastAnswers;
    }

    private HashMap<Long, String> usersLastAnswers = new HashMap<>();
    final private String[] keys = new DB().getAllSubscribeKey();

    public String[] getKeys() {
        return keys;
    }

    public HashMap<String, String[]> getCommands() {
        return commands;
    }

    public Storage(){
        commands.put("/start", new String[]{"Привет, я бот для проверки подписок", "Введите /subscribe для подключения подписки"});
        commands.put("/unsubscribe", new String[]{"Вы точно уверены что хотите отписаться?", "Введите ДА если хотите отписаться, или НЕТ если хотите оставить подписку"});
        commands.put("/subscribe", new String[]{"Введите ключ для получения подписки:"});
        commands.put("/info", new String[]{"Вот информация о вашей подписке:"});
    }
}
