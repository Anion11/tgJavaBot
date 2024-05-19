package org.example.entity;
import java.util.HashMap;

public class Subscribe {
    private int subscribe_id;
    private String subscribe_type;
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getSubscribeId() {
        return subscribe_id;
    }

    public void setSubscribeId(int subscribe_id) {
        this.subscribe_id = subscribe_id;
    }

    public void setSubscribeType(String subscribe_type) {
        this.subscribe_type = subscribe_type;
    }
    public String getSubscribeType() {
        return subscribe_type;
    }

    @Override
    public String toString() {
        return "Подписка на данный момент активна : " + subscribe_type;
    }
}
