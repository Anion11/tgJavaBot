package org.example.DB.AppUserDAO;

import org.example.entity.AppUser;
import org.example.entity.Subscribe;

import java.util.ArrayList;

public interface IAppUserDAO {
    AppUser getUserInfo(Long id);
    boolean createAppUser(AppUser entity);
    ArrayList<AppUser> getAllSubscribers();
    boolean unSubscribeUser(Long id);
    boolean subscribeUser(Long id, Subscribe subscribe);
}
