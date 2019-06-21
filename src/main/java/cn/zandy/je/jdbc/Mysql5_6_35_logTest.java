package cn.zandy.je.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mysql5_6_35_logTest {

    private static final int SLEEP_SECONDS = 1;

    private static AtomicBoolean loopSwitch = new AtomicBoolean(true);

    public static void main(String[] args) {
        testLockWait();
    }

    /*-------------------------- test begin --------------------------*/

    private static void testLockWait() {
        Thread d = new Thread(() -> {
            while (true) {
                select();
                sleep(TimeUnit.MILLISECONDS, 300);
            }
        }, "select-thread");
        d.setDaemon(true);
        d.start();

        /* -- [TAG] :: 通过死锁来体现 mysql 的行级锁 ------------ */
        // "sleep-thread" 里【事务的提交】依赖于 "not-sleep-thread" 【事务提交】之后的 将 loopSwitch 设置为 false;
        // 如果 "sleep-thread" 中优先执行 executeUpdate(获得行级锁)，
        // 那么 "not-sleep-thread" 里执行 executeUpdate 时要等待 "sleep-thread" 中【事务的提交】
        // 这种情况本程序会产生死锁（一直 loop）
        /*
            ------ sql ------
            select * from information_schema.innodb_trx;        -- 当前运行的所有事务
            select * from information_schema.innodb_locks;      -- 当前出现的锁
            select * from information_schema.innodb_lock_waits; -- 锁等待的对应关系

            ------ java ------ 异常信息 (Lock wait time 50s 左右, 配置在哪里？)
            com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction
              at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
              at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
              at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
              at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
              at com.mysql.jdbc.Util.handleNewInstance(Util.java:425)
              at com.mysql.jdbc.Util.getInstance(Util.java:408)
              at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:952)
              at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3973)
              at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3909)
              at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2527)
              at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2680)
              at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2480)
              at com.mysql.jdbc.StatementImpl.executeUpdateInternal(StatementImpl.java:1552)
              at com.mysql.jdbc.StatementImpl.executeLargeUpdate(StatementImpl.java:2607)
              at com.mysql.jdbc.StatementImpl.executeUpdate(StatementImpl.java:1480)
              at cn.zandy.je.jdbc.TestMysql.updateCauseDiedLock(TestMysql.java:63)
              at cn.zandy.je.jdbc.TestMysql.lambda$main$2(TestMysql.java:40)
              at java.lang.Thread.run(Thread.java:745)

            ------ reference ------
            https://segmentfault.com/a/1190000015314171
         */
        new Thread(() -> updateCauseDiedLock("url='9'", true), "sleep-thread").start();
        new Thread(() -> updateCauseDiedLock("app_type=9", false), "not-sleep-thread").start();
        /* -- [END] :: 通过死锁来体现 mysql 的行级锁 ------------ */

        sleep(TimeUnit.SECONDS, SLEEP_SECONDS + 2);
    }

    /*-------------------------- test end --------------------------*/

    private static void updateCauseDiedLock(String param, boolean sleep) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/track", "root", "lingdian");
            printf("get connection.");

            conn.setAutoCommit(false);
            printf("not auto commit.");

            Statement st = conn.createStatement();
            printf("get statement.");

            int i = st.executeUpdate("update jdbc_test set " + param + " where id=1");
            printf("executeUpdate, result=" + i);

            if (sleep) {
                printf("begin sleep.");
                while (loopSwitch.get()) {
                    TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
                }
                printf("end sleep.");
            }

            conn.commit();
            printf("commit tx.");
            if (!sleep) {
                loopSwitch.set(false);
            }

            st.close();
        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    printf("rollback tx");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
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

    private static void select() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/track", "root", "lingdian");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM jdbc_test WHERE id=1");

            rs.next();
            printf("url=" + rs.getString("url") + ", app_type=" + rs.getInt("app_type"));

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
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printf(String msg) {
        System.out.printf("[%16s] [%-28s] %s.\n", Thread.currentThread().getName(), new java.util.Date(), msg);
    }

    private static void sleep(TimeUnit timeUnit, long l) {
        try {
            timeUnit.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
