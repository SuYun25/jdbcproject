package stock.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) props.load(in);
        } catch (Exception e) {
            // 로컬 설정 못 읽어도 환경변수로 대체 가능하게 둠
        }
    }

    private static String get(String key, String envKey) {
        String v = props.getProperty(key);
        if (v != null && !v.isBlank()) return v;
        return System.getenv(envKey);
    }

    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        String url  = get("db.url", "DB_URL");
        String user = get("db.user", "DB_USER");
        String pass = get("db.pass", "DB_PASS");

        return DriverManager.getConnection(url, user, pass);
    }
}