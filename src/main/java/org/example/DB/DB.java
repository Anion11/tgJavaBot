package org.example.DB;

import org.example.entity.AppUser;
import org.example.entity.Subscribe;
import org.example.entity.SubscribePost;

import java.sql.*;
import java.util.ArrayList;

public class DB extends config {
    public SubscribePost getRandomSubscribePost(String subType) {
        SubscribePost randPost = new SubscribePost();
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE type='" + subType + "' ORDER BY RANDOM() LIMIT 1");
                while (rs.next()) {
                    randPost.setDescr(rs.getString("descr"));
                    randPost.setSrc(rs.getString("src"));
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return randPost;
    }

    public String[] getAllSubscribeKey() {
        ArrayList<String> keys = new ArrayList<>();
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT key FROM subscribe_key");
                while (rs.next()) {
                    String key = rs.getString("key");
                    if (key != null) keys.add(key);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys.toArray(new String[0]);
    }
    public Subscribe getSubscribe(String key) {
        Subscribe subscribe = new Subscribe();
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe_key WHERE key='" + key + "'");
                while (rs.next()) {
                    subscribe.setSubscribeId(rs.getInt("id"));
                    subscribe.setSubscribeType(rs.getString("type"));
                    subscribe.setActive(true);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subscribe;
    }
    public boolean createUser(AppUser user) {
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO user_table (telegram_user_id, first_name, last_name, user_name, subscribe) VALUES (?, ?, ?, ?, ?)");
                st.setLong(1, user.getTelegramUserId());
                st.setString(2, user.getFirstName());
                st.setString(3, user.getLastName());
                st.setString(4, user.getUsername());
                st.setInt(5, user.getSubscribe().getSubscribeId());
                st.executeUpdate();
                st.close();
            } finally {
                con.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public AppUser getUserData(Long id) {
        AppUser user = null;
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_table WHERE telegram_user_id = " + id);
                Subscribe sub = new Subscribe();
                user = new AppUser();
                while (rs.next()) {
                    user.setId(rs.getLong("id"));
                    user.setTelegramUserId(rs.getLong("telegram_user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("user_name"));
                    sub.setSubscribeId(rs.getInt("subscribe"));
                    if (rs.getInt("subscribe") != 0) sub.setActive(true);
                    else sub.setActive(false);
                }
                rs.close();
                rs = stmt.executeQuery("SELECT * FROM user_subscribe WHERE subscribe_id = " + sub.getSubscribeId());
                while (rs.next()) {
                    sub.setSubscribeType(rs.getString("subscribe_type"));
                }
                user.setSubscribe(sub);
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    public boolean unSubscribeUser(Long id) {
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("UPDATE user_table SET subscribe=null WHERE telegram_user_id = " + id);
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM user_subscribe WHERE user_id = " + id);
                st.executeUpdate();
                st.close();
            } finally {
                con.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean subscribeUser(Long id, Subscribe subscribe) {
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("UPDATE user_table SET subscribe=" + subscribe.getSubscribeId() + " WHERE telegram_user_id = " + id);
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM subscribe_key WHERE id = '" + subscribe.getSubscribeId() + "'");
                st.executeUpdate();
                st = con.prepareStatement("INSERT INTO user_subscribe (user_id, subscribe_id, subscribe_type) VALUES (?, ?, ?)");
                st.setLong(1, id);
                st.setInt(2, subscribe.getSubscribeId());
                st.setString(3, subscribe.getSubscribeType());
                st.executeUpdate();
                st.close();
            } finally {
                con.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<AppUser> getAllSubscribers() {
        ArrayList<AppUser> users = new ArrayList<>();
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_subscribe");
                while (rs.next()) {
                    AppUser user = new AppUser();
                    Long id = rs.getLong("user_id");
                    user.setTelegramUserId(id);
                    Subscribe sub = new Subscribe();
                    sub.setSubscribeType(rs.getString("subscribe_type"));
                    user.setSubscribe(sub);
                    users.add(user);
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}