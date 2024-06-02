package org.example.entity;
/**
 * Класс, представляющий подписку пользователя.
 */
public class Subscribe {
    /** Идентификатор подписки. */
    private int subscribe_id;
    /** Тип подписки. */
    private String subscribe_type;
    /** Флаг активности подписки. */
    private Boolean active;
    /** Далее представлены геттеры и сеттеры для полей класса */

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
