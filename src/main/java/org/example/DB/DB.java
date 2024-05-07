package org.example.DB;

import org.example.entity.AppUser;

import java.sql.*;
import java.util.ArrayList;

public class DB extends config {
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
    public boolean createUser(AppUser user) {
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("INSERT INTO user_table (telegram_user_id, first_name, last_name, user_name, is_active) VALUES (?, ?, ?, ?, ?)");
                st.setLong(1, user.getTelegramUserId());
                st.setString(2, user.getFirstName());
                st.setString(3, user.getLastName());
                st.setString(4, user.getUsername());
                st.setBoolean(5, user.getActive());
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
                while (rs.next()) {
                    user = new AppUser();
                    user.setId(rs.getLong("id"));
                    user.setTelegramUserId(rs.getLong("telegram_user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("user_name"));
                    user.setActive(rs.getBoolean("is_active"));
                }
                rs.close();
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
                PreparedStatement st = con.prepareStatement("UPDATE user_table SET is_active=false WHERE telegram_user_id = " + id);
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
    public boolean subscribeUser(Long id, String key) {
        try {
            Connection con  = DriverManager.getConnection(url, login, password);
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("UPDATE user_table SET is_active=true WHERE telegram_user_id = " + id);
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM subscribe_key WHERE key = '" + key + "'");
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
}