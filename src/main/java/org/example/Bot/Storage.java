package org.example.Bot;

import org.example.DB.DB;
import java.util.HashMap;
import java.util.Set;

public class Storage {
    final private HashMap<String, String[]> commands = new HashMap<>();

    public HashMap<Long, String> getUsersLastAnswers() {
        return usersLastAnswers;
    }

    private final HashMap<Long, String> usersLastAnswers = new HashMap<>();

    public String[] getKeys() {
        return new DB().getAllSubscribeKey();
    }

    public HashMap<String, String[]> getCommands() {
        return commands;
    }
    public Set<String> getStingCommands() {
        return commands.keySet();
    }

    public Storage(){
        commands.put("/start", new String[]{"Привет, я бот для проверки подписок", "Введите /subscribe для подключения подписки"});
        commands.put("/unsubscribe", new String[]{"Вы точно уверены что хотите отписаться?"});
        commands.put("/subscribe", new String[]{"Введите ключ для получения подписки:"});
        commands.put("/info", new String[]{"Вот информация о вашей подписке:"});
    }
}
