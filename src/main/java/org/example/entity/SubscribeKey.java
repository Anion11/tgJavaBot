package org.example.entity;

public class SubscribeKey {
    private int subscribe_key_id;
    private String subscribe_type;
    private String key;

    public int getSubscribe_key_id() {
        return subscribe_key_id;
    }

    public String getSubscribe_type() {
        return subscribe_type;
    }

    public String getKey() {
        return key;
    }

    public void setSubscribe_key_id(int subscribe_key_id) {
        this.subscribe_key_id = subscribe_key_id;
    }

    public void setSubscribe_type(String subscribe_type) {
        this.subscribe_type = subscribe_type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Подписка на данный момент активна : " + subscribe_type;
    }
}
