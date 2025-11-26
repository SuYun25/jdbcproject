package stock.dao;

import java.sql.*;
import java.util.ArrayList;

import stock.dto.Account;
import stock.util.DBUtil;

public class AccountDAO {

    // 계좌 개설
    public int createAccount(int userId, int accountId) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "INSERT INTO ACCOUNTS (account_id, user_id, balance, create_date) "
                   + "VALUES (?, ?, 0, SYSDATE)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);

        int result = ps.executeUpdate();
        conn.close();
        return result;
    }

    // 유저의 모든 계좌 조회
    public ArrayList<Account> getAccounts(int userId) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "SELECT * FROM ACCOUNTS WHERE user_id = ? ORDER BY account_id";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        ArrayList<Account> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getInt("balance"),
                    rs.getString("create_date")
            ));
        }

        conn.close();
        return list;
    }

    // 특정 계좌 조회 (로그인한 유저 소유인지 확인)
    public Account getAccount(int accountId, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "SELECT * FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);

        ResultSet rs = ps.executeQuery();
        Account acc = null;

        if (rs.next()) {
            acc = new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getInt("balance"),
                    rs.getString("create_date")
            );
        }

        conn.close();
        return acc;
    }

    // 잔고 업데이트
    public void updateBalance(int accountId, int newBalance) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "UPDATE ACCOUNTS SET balance = ? WHERE account_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newBalance);
        ps.setInt(2, accountId);

        ps.executeUpdate();
        conn.close();
    }

    // 계좌 삭제
    public int deleteAccount(int accountId, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "DELETE FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accountId);
        ps.setInt(2, userId);

        int result = ps.executeUpdate();
        conn.close();
        return result;
    }

    // 충전
    public int deposit(int accountId, int amount, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();

        String sql = "UPDATE ACCOUNTS SET balance = balance + ? "
                   + "WHERE account_id = ? AND user_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, amount);
        ps.setInt(2, accountId);
        ps.setInt(3, userId);

        int result = ps.executeUpdate();
        conn.close();
        return result;
    }

    // 이체 기능
    public int transfer(int fromAcc, int toAcc, int amount, int userId) throws Exception {
        Connection conn = DBUtil.getConnection();

        conn.setAutoCommit(false);

        // 1. 출금 계좌 확인
        String sql1 = "SELECT balance FROM ACCOUNTS WHERE account_id = ? AND user_id = ?";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.setInt(1, fromAcc);
        ps1.setInt(2, userId);
        ResultSet rs1 = ps1.executeQuery();

        if (!rs1.next()) {
            conn.rollback();
            conn.close();
            return -1; // 출금 계좌 오류
        }

        int balance = rs1.getInt("balance");
        if (balance < amount) {
            conn.rollback();
            conn.close();
            return -2; // 잔액 부족
        }

        // 2. 입금 계좌 확인
        String sql2 = "SELECT account_id FROM ACCOUNTS WHERE account_id = ?";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setInt(1, toAcc);
        ResultSet rs2 = ps2.executeQuery();

        if (!rs2.next()) {
            conn.rollback();
            conn.close();
            return -3; // 입금 계좌 없음
        }

        // 3. 출금
        String sql3 = "UPDATE ACCOUNTS SET balance = balance - ? WHERE account_id = ?";
        PreparedStatement ps3 = conn.prepareStatement(sql3);
        ps3.setInt(1, amount);
        ps3.setInt(2, fromAcc);
        ps3.executeUpdate();

        // 4. 입금
        String sql4 = "UPDATE ACCOUNTS SET balance = balance + ? WHERE account_id = ?";
        PreparedStatement ps4 = conn.prepareStatement(sql4);
        ps4.setInt(1, amount);
        ps4.setInt(2, toAcc);
        ps4.executeUpdate();

        conn.commit();
        conn.close();
        return 1;
    }
}
