package stock.dao;

import java.sql.*;
import java.util.ArrayList;
import stock.dto.Stock;
import stock.util.DBUtil;

public class StockDAO {

	// 전체 종목 조회
	public ArrayList<Stock> getAllStocks() throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM STOCKS ORDER BY stock_id";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		ArrayList<Stock> list = new ArrayList<>();

		while (rs.next()) {
			list.add(new Stock(rs.getInt("stock_id"), rs.getString("stock_name"), rs.getInt("current_price")));
		}

		conn.close();
		return list;
	}

	// 종목 명으로 검색 (정확)
	public Stock getStockByName(String name) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM STOCKS WHERE stock_name = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		Stock stock = null;

		if (rs.next()) {
			stock = new Stock(rs.getInt("stock_id"), rs.getString("stock_name"), rs.getInt("current_price"));
		}

		conn.close();
		return stock;
	}

	// LIKE 검색
	public ArrayList<Stock> searchByName(String name) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM STOCKS WHERE stock_name LIKE ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, "%" + name + "%");

		ResultSet rs = ps.executeQuery();
		ArrayList<Stock> list = new ArrayList<>();

		while (rs.next()) {
			list.add(new Stock(rs.getInt("stock_id"), rs.getString("stock_name"), rs.getInt("current_price")));
		}

		conn.close();
		return list;
	}

	// stock_id로 조회
	public Stock getStockById(int stockId) throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "SELECT * FROM STOCKS WHERE stock_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, stockId);

		ResultSet rs = ps.executeQuery();
		Stock s = null;

		if (rs.next()) {
			s = new Stock(rs.getInt("stock_id"), rs.getString("stock_name"), rs.getInt("current_price"));
		}

		conn.close();
		return s;
	}

	public void updateRandomPrices() throws Exception {
		Connection conn = DBUtil.getConnection();

		String sql = "UPDATE stocks " + "SET current_price = current_price + TRUNC(DBMS_RANDOM.VALUE(-200, 200)) "
				+ "WHERE current_price + TRUNC(DBMS_RANDOM.VALUE(-200, 200)) > 100";

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();

		conn.close();
	}
}
