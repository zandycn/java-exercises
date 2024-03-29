package cn.zandy.je.jdk.concurrency.deadlock;

import java.util.concurrent.CountDownLatch;

import static cn.zandy.je.Printer.printf;

/**
 * 模拟测试死锁的一个场景.
 * 并简要描述 jstack 输出结果的含义.
 *
 * Created by zandy on 2019/9/27.
 */
public class DLSimulate1 {

    /**
     * ➜  ~ jstack -l [pid]
     * <pre>
     * "transfer-t-b2a" #12 prio=5 os_prio=31 tid=0x00007fe915836800 nid=0x5b03 waiting for monitor entry [0x0000700010816000]
     *     java.lang.Thread.State: BLOCKED (on object monitor)
     *         at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.transfer(DLSimulate1.java:59)
     *         - waiting to lock <0x000000076ac1fb18> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *         - locked <0x000000076ac1fb28> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *         at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.lambda$main$1(DLSimulate1.java:42)
     *         at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$$Lambda$2/1534030866.run(Unknown Source)
     *         at java.lang.Thread.run(Thread.java:745)
     *
     *     Locked ownable synchronizers:
     *         - None
     *
     * "transfer-t-a2b" #11 prio=5 os_prio=31 tid=0x00007fe91585a800 nid=0x5903 waiting for monitor entry [0x0000700010713000]
     *     java.lang.Thread.State: BLOCKED (on object monitor)
     *         at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.transfer(DLSimulate1.java:59)
     *         - waiting to lock <0x000000076ac1fb28> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *         - locked <0x000000076ac1fb18> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *         at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.lambda$main$0(DLSimulate1.java:33)
     *         at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$$Lambda$1/885284298.run(Unknown Source)
     *         at java.lang.Thread.run(Thread.java:745)
     *
     *     Locked ownable synchronizers:
     *         - None
     *
     * ............
     *
     * Found one Java-level deadlock:
     * =============================
     * "transfer-t-b2a":
     *     waiting to lock monitor 0x00007fe9180116a8 (object 0x000000076ac1fb18, a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account),
     *     which is held by "transfer-t-a2b"
     * "transfer-t-a2b":
     *     waiting to lock monitor 0x00007fe91800ee18 (object 0x000000076ac1fb28, a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account),
     *     which is held by "transfer-t-b2a"
     *
     * Java stack information for the threads listed above:
     * ===================================================
     * "transfer-t-b2a":
     *     at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.transfer(DLSimulate1.java:59)
     *     - waiting to lock <0x000000076ac1fb18> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *     - locked <0x000000076ac1fb28> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *     at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.lambda$main$1(DLSimulate1.java:42)
     *     at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$$Lambda$2/1534030866.run(Unknown Source)
     *     at java.lang.Thread.run(Thread.java:745)
     * "transfer-t-a2b":
     *     at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.transfer(DLSimulate1.java:59)
     *     - waiting to lock <0x000000076ac1fb28> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *     - locked <0x000000076ac1fb18> (a cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account)
     *     at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1.lambda$main$0(DLSimulate1.java:33)
     *     at cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$$Lambda$1/885284298.run(Unknown Source)
     *     at java.lang.Thread.run(Thread.java:745)
     *
     * Found 1 deadlock.
     * </pre>
     *
     * <pre>
     *     "#" 后的数字表示 当前 thread 的 ID，即 {@link java.lang.Thread#getId()}
     *     prio 表示线程的权重，可以参考 Thread.java 类注释，默认值 {@link java.lang.Thread#NORM_PRIORITY}
     *     tid - Java memory address of its internal Thread control structure
     *     nid - `top -Hp [pid]` 列表中某一行的 PID(我理解是操作系统给该线程分配的 process ID) 转成16进制
     *           疑问 `top -Hp [pid]` 不显示 阻塞的 线程? 答: 是显示的！！！
     *
     *     address:
     *       0x0000700010816000  "transfer-t-b2a" 线程栈的起始地址?
     *       0x0000700010713000  "transfer-t-a2b" 线程栈的起始地址?
     *       0x00007fe9180116a8
     *       0x00007fe91800ee18
     *       0x000000076ac1fb18  锁对象 cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account 地址(Account a指向)?
     *       0x000000076ac1fb28  锁对象 cn.zandy.je.jdk.concurrent.deadlock.DLSimulate1$Account 地址(Account b指向)?
     * </pre>
     *
     * <pre>
     *     延伸: Linux 下进程和线程都会有自己的ID, 这个ID叫做PID, PID不是特指进程ID, 线程ID也可以叫做PID.
     *       - getpid() : 获取进程ID
     *       - pthread_self() : 获取进程内线程的唯一标识ID，这个ID在某一进程中是唯一的，在不同的进程中创建的线程可能出现ID值相同的情况
     *       - syscall(SYS_gettid) : 获取操作系统中唯一的线程ID
     *
     *     查看进程或线程常用的命令(以下 [pid] 指进程PID):
     *       - top -Hp [pid]
     *       - ps p [pid] -L -o pcpu,pmem,ppid,pid,tid,tname,time,stat,psr,cmd | sort -rn -k1
     *       - jstack -l [pid]           (仅限于 java 环境)
     *       - ps -eLf | grep [pid]      (e显示全部进程, L显示全部线程, f全格式输出)
     *       - pstree -p [pid]
     * </pre>
     */
    public static void main(String[] args) {
        Account a = new Account();
        a.setMoney(100);

        Account b = new Account();
        b.setMoney(200);

        CountDownLatch runTaskGate = new CountDownLatch(1);

        new Thread(() -> {
            printf("into task, thread.getId()=" + Thread.currentThread().getId());
            try {
                runTaskGate.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            transfer(a, b, 50);
        }, "transfer-t-a2b").start();

        new Thread(() -> {
            printf("into task, thread.getId()=" + Thread.currentThread().getId());
            try {
                runTaskGate.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            transfer(b, a, 100);
        }, "transfer-t-b2a").start();

        runTaskGate.countDown();
    }

    private static void transfer(Account a, Account b, int money) {
        synchronized (a) {
            a.setMoney(a.getMoney() - money);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (b) {
                b.setMoney(b.getMoney() + money);
            }
        }

        printf("transfer success");
        printf("Account a:" + a);
        printf("Account b:" + b);
    }

    private static class Account {

        private int money;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        @Override
        public String toString() {
            return "money:" + money;
        }
    }
}
