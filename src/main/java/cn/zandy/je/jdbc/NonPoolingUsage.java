package cn.zandy.je.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Usage: 一个【普通的】数据库连接过程.
 * <p>
 * 1.加载驱动程序 (参见 com.mysql.jdbc.Driver 中 static 块 java.sql.DriverManager.registerDriver(new Driver()); )
 * 2.通过 java.sql.DriverManager 到得一个与数据库连接的句柄
 * 3.通过连接句柄绑定要执行的语句
 * 4.接收执行结果
 * 5.可选的对结果的处理
 * 6.必要的关闭和数据库连接的关闭
 */
public class NonPoolingUsage {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // 1 加载驱动程序：主要是为了注册驱动程序
            Class.forName("com.mysql.jdbc.Driver");

            // 2 通过 java.sql.DriverManager 到得一个与数据库连接的句柄
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/track", "root", "lingdian");

            // 3 通过连接句柄绑定要执行的语句
            // 4 接收执行结果
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM jdbc_test");

            // 5 可选的对结果的处理
            if (rs.next()) {
                do {
                    System.out.printf("%-2d : %s\n", rs.getInt(1), rs.getString("resource_name"));
                } while (rs.next());
            } else {
                System.out.println("未查询到任何结果！");
            }

            // 6 必要的关闭和数据库连接的关闭
            rs.close();
            st.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Load jdbc driver cause exception: ");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    // 6 必要的关闭和数据库连接的关闭
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
