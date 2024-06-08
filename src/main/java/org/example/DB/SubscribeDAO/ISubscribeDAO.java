package org.example.DB.SubscribeDAO;

import org.example.entity.Subscribe;

public interface ISubscribeDAO {
    Subscribe getSubscribe(String key);
    Subscribe[] getAllSubscribeKey();
    boolean unSubscribeUser(Long id, Subscribe sub);
    boolean subscribeUser(Long id, Subscribe subscribe);

    Subscribe getSubscribeByType(Long userId, String type);
}
