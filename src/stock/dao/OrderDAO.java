package stock.dao;

import java.sql.*;
import java.util.ArrayList;

import stock.dto.Order;
import stock.util.DBUtil;

public class OrderDAO {

	// 매수/매도 주문 저장
	public void insertOrder(int accountId, int stockId, int qty, int price, String type) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "INSERT INTO ORDERS (order_id, account_id, stock_id, order_type, quantity, price_at_trade, trade_date) "
				+ "VALUES (seq_order.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accountId);
		ps.setInt(2, stockId);
		ps.setString(3, type);
		ps.setInt(4, qty);
		ps.setInt(5, price);

		ps.executeUpdate();
		conn.close();
	}

	// 특정 계좌의 거래내역 조회
	public ArrayList<Order> getOrders(int accountId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM ORDERS WHERE account_id = ? ORDER BY order_id DESC";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accountId);

		ResultSet rs = ps.executeQuery();
		ArrayList<Order> list = new ArrayList<>();

		while (rs.next()) {

			String tradeDate = null;
			if (rs.getDate("trade_date") != null) {
				tradeDate = rs.getDate("trade_date").toString();
			}

			list.add(new Order(rs.getInt("order_id"), rs.getInt("account_id"), rs.getInt("stock_id"),
					rs.getString("order_type"), rs.getInt("quantity"), rs.getInt("price_at_trade"), tradeDate));
		}

		conn.close();
		return list;
	}
}
