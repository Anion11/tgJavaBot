package org.example.entity;

public class Subscribe {
    private int subscribe_id;
    private String subscribe_type;
    private String key;

    public int getSubscribeId() {
        return subscribe_id;
    }

    public String getSubscribeType() {
        return subscribe_type;
    }

    public String getKey() {
        return key;
    }

    public void setSubscribeId(int subscribe_key_id) {
        this.subscribe_id = subscribe_key_id;
    }

    public void setSubscribeType(String subscribe_type) {
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
