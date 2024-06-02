package org.example.entity;
/**
 * Класс, представляющий пост подписки.
 */
public class SubscribePost {
    /** Описание поста. */
    private String descr;
    /** Источник до картинки к посту. */
    private String src;
    /** Далее представлены геттеры и сеттеры для полей класса */

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setSrc(String rsc) {
        this.src = rsc;
    }

    public String getDescr() {
        return descr;
    }

    public String getSrc() {
        return src;
    }
}
