package stock.dao;

import java.sql.*;
import java.util.ArrayList;

import stock.dto.Holding;
import stock.util.DBUtil;

public class HoldingDAO {

	// 특정 계좌의 전체 보유 주식 조회
	public ArrayList<Holding> getHoldingsByAccount(int accId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM HOLDINGS WHERE account_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accId);

		ResultSet rs = ps.executeQuery();
		ArrayList<Holding> list = new ArrayList<>();

		while (rs.next()) {
			list.add(new Holding(rs.getInt("holding_id"), rs.getInt("account_id"), rs.getInt("stock_id"),
					rs.getInt("quantity"), rs.getInt("avg_price")));
		}

		conn.close();
		return list;
	}

	// 특정 종목 하나 조회
	public Holding getHolding(int accId, int stockId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM HOLDINGS WHERE account_id = ? AND stock_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accId);
		ps.setInt(2, stockId);

		ResultSet rs = ps.executeQuery();
		Holding h = null;

		if (rs.next()) {
			h = new Holding(rs.getInt("holding_id"), rs.getInt("account_id"), rs.getInt("stock_id"),
					rs.getInt("quantity"), rs.getInt("avg_price"));
		}

		conn.close();
		return h;
	}

	// 신규 보유 추가
	public void insertHolding(int accId, int stockId, int qty, int price) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "INSERT INTO HOLDINGS VALUES (seq_holding.NEXTVAL, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accId);
		ps.setInt(2, stockId);
		ps.setInt(3, qty);
		ps.setInt(4, price);

		ps.executeUpdate();
		conn.close();
	}

	// 기존 보유 업데이트 (평단 자동 계산 포함)
	public void updateHolding(int holdingId, int newQty, int newAvg) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "UPDATE HOLDINGS SET quantity = ?, avg_price = ? WHERE holding_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, newQty);
		ps.setInt(2, newAvg);
		ps.setInt(3, holdingId);

		ps.executeUpdate();
		conn.close();
	}

	// 수량 0이면 삭제
	public void deleteHolding(int holdingId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "DELETE FROM HOLDINGS WHERE holding_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, holdingId);

		ps.executeUpdate();
		conn.close();
	}
}
