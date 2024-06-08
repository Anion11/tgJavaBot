package org.example.DB.SubscribeDAO;

import org.example.DB.DB;
import org.example.entity.Subscribe;

import java.sql.*;
import java.util.ArrayList;

public class SubscribeDAO implements ISubscribeDAO {
    private DB db;

    public SubscribeDAO() {
        this.db = new DB();
    }

    @Override
    public Subscribe getSubscribe(String key) {
        Subscribe subscribe = new Subscribe();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE key ='" + key + "'");
                while (rs.next()) {
                    subscribe.setSubscribeId(rs.getInt("id"));
                    subscribe.setSubscribeType(rs.getString("type"));
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

    @Override
    public Subscribe getSubscribeByType(Long userId, String type) {
        Subscribe subscribe = new Subscribe();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe WHERE user_id= " + userId + " AND type = '" + type + "'");
                while (rs.next()) {
                    subscribe.setSubscribeId(rs.getInt("id"));
                    subscribe.setSubscribeType(rs.getString("type"));
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

    @Override
    public boolean unSubscribeUser(Long id, Subscribe sub) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("DELETE FROM subscribe WHERE user_id = " + id + " AND type = '" + sub.getSubscribeType() + "'");
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM user_subscribe WHERE subscribe_id = " + sub.getSubscribeId());
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

    @Override
    public boolean subscribeUser(Long id, Subscribe subscribe) {
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                PreparedStatement st = con.prepareStatement("UPDATE subscribe SET key = ?, user_id = ? WHERE id = ?");
                st.setNull(1, Types.VARCHAR);
                st.setLong(2, id);
                st.setInt(3, subscribe.getSubscribeId());
                st.executeUpdate();
                st = con.prepareStatement("INSERT INTO user_subscribe (subscribe_id, user_id) VALUES (?, ?)");
                st.setLong(1, subscribe.getSubscribeId());
                st.setLong(2, id);
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

    @Override
    public Subscribe[] getAllSubscribeKey() {
        ArrayList<Subscribe> keys = new ArrayList<Subscribe>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM subscribe");
                while (rs.next()) {
                    Subscribe SubKey = new Subscribe();
                    String key = rs.getString("key");
                    String type = rs.getString("type");
                    if (key != null && type != null) {
                        SubKey.setKey(key);
                        SubKey.setSubscribeType(type);
                        keys.add(SubKey);
                    };
                }
                rs.close();
                stmt.close();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys.toArray(new Subscribe[0]);
    }
}
