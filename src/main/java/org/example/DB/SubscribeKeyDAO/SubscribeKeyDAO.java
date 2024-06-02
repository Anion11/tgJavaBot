package org.example.DB.SubscribeKeyDAO;

import org.example.DB.DB;
import org.example.DB.AppUserDAO.IAppUserDAO;
import org.example.entity.SubscribeKey;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SubscribeKeyDAO implements ISubscribeKeyDAO {
    private DB db;

    public SubscribeKeyDAO() {
        this.db = new DB();
    }

    @Override
    public SubscribeKey[] getAllSubscribeKey() {
        ArrayList<SubscribeKey> keys = new ArrayList<SubscribeKey>();
        try {
            Connection con  = db.getConnection();
            Class.forName("org.postgresql.Driver");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT key FROM subscribe_key");
                while (rs.next()) {
                    SubscribeKey SubKey = new SubscribeKey();
                    String key = rs.getString("key");
                    if (key != null) {
                        SubKey.setKey(key);
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
        return keys.toArray(new SubscribeKey[0]);
    }
}
