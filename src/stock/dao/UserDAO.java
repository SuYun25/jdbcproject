package stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import stock.dto.User;
import stock.util.DBUtil;

public class UserDAO {

    // 회원가입
    public int signup(User user) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "INSERT INTO USERS (user_id, username, password, reg_date) "
                + "VALUES (seq_user.NEXTVAL, ?, ?, SYSDATE)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());

        int result = ps.executeUpdate();
        conn.close();
        return result;
    }

    // 로그인
    public User login(String username, String password) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        User user = null;

        if (rs.next()) {
            user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRegDate(rs.getString("reg_date"));
        }

        conn.close();
        return user;
    }
    
}
