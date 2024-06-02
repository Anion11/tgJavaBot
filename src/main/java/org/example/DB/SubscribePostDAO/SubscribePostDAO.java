package org.example.DB.SubscribePostDAO;

import org.example.DB.DB;
import org.example.entity.SubscribePost;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SubscribePostDAO  implements ISubscribePostDAO {
    private DB db;

    public SubscribePostDAO() {
        this.db = new DB();
    }
    @Override
    public SubscribePost getRandomSubscribePost(String subType) {
        SubscribePost randPost = new SubscribePost();
        try {
            Connection con  = db.getConnection();
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
}
