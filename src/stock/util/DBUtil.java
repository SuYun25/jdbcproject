package stock.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

	private static final String URL = "jdbc:oracle:thin:@192.168.0.35:1521:XE";
	private static final String USER = "stockuser";
	private static final String PASS = "1234";

	public static Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return DriverManager.getConnection(URL, USER, PASS);
	}
}
