package org.example.DB.SubscribePostDAO;

import org.example.entity.SubscribePost;

public interface ISubscribePostDAO {
    SubscribePost getRandomSubscribePost(String subType);
}
