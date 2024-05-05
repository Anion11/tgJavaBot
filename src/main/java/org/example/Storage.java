package org.example;

import java.util.HashMap;

public class Storage {
    final private HashMap<String, String> messages = new HashMap<>();

    public HashMap<String, String> getMessages() {
        return messages;
    }

    public Storage() {
        messages.put("/start", "Привет");
        messages.put("/stop", "Пока");
    }
}
