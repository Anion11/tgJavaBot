package org.example.DB.SubscribeDAO;

import org.example.DB.DB;
import org.example.entity.Subscribe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
}
