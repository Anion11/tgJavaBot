package org.example.DB.AppUserDAO;

import org.example.entity.AppUser;

public interface IAppUserDAO {
    AppUser getUserInfo(Long id);
    boolean createAppUser(AppUser entity);
}
