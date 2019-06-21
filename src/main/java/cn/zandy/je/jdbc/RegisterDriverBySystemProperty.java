package cn.zandy.je.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RegisterDriverBySystemProperty {

    public static void main(String[] args) {
        String drivers = "org.gjt.mm.mysql.Driver:com.mysql.jdbc.Driver";

        // 参见: DriverManager.loadInitialDrivers()
        System.setProperty("jdbc.drivers", drivers);

        Connection conn = null;
        try {
            // JDBC会按顺序搜索，直到找到第一个能成功连结该URL的驱动程序
            // 参见: getConnection worker method 中的 for 循环实现
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/track?user=root&password=lingdian");
            System.out.println(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
