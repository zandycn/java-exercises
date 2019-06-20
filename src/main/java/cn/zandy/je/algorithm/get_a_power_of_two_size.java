package cn.zandy.je.algorithm;

/**
 * 命题：查找比给定整数大的第一个 2^n
 * 方案：用二进制思维解决
 * <p>
 * 由于二进制的特性，决定了 2^n 对应的二进制数字只有一位是1，其它位都位0
 * 对于一个 非2^n 的正整数，寻找比其大的第一个 2^n，方法是:
 * 1.把 该数字最高位 及其右面的每一位都变成1
 * 2.在得到的数字基础上加1
 * <p>
 * 比如: 数字9 (二进制 0000 1010)，寻找比其稍大的 2^n:
 * 第一步 0000 1111
 * 第二步 0000 1111 + 0000 0001 = 0001 0000  十进制为16
 */
public class get_a_power_of_two_size {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Returns a power of two size for the given target capacity.
     */
    public static int tableSizeFor(int cap) {
        printf("cap", Integer.toBinaryString(cap));

        /* "cap - 1" 是为了实现当 cap 为等比数列 2^n 通项时，通过该方法获取到的是当前值，而不是下一项 */
        int n = cap - 1;
        //int n = cap;
        printf("n", Integer.toBinaryString(n));

        n |= n >>> 1; // 第一次 n|=n>>>1 之后，n 的高位前(2)位都变为1
        printf("(n |= n >>> 1)", Integer.toBinaryString(n));

        n |= n >>> 2; // 再进行 n|=n>>>2 之后，n 的高位前(4)位都变为1，以此类推 n|=n>>>4,n|=n>>>8,n|=n>>>16,n|=n>>>32 ……
        printf("(n |= n >>> 2)", Integer.toBinaryString(n));

        n |= n >>> 4;
        printf("(n |= n >>> 4)", Integer.toBinaryString(n));

        n |= n >>> 8;
        printf("(n |= n >>> 8)", Integer.toBinaryString(n));

        n |= n >>> 16;
        printf("(n |= n >>> 16)", Integer.toBinaryString(n));

        // return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;

        /* -- 分析返回值 --------------------------*/
        if (n < 0) {
            // -- 负数都返回 1 ------------ //
            System.out.println("n < 0");
            return 1;
        }

        if (n >= MAXIMUM_CAPACITY) {
            // -- 大于 MAXIMUM_CAPACITY 的都返回 MAXIMUM_CAPACITY ------------ //
            System.out.println("n >= " + MAXIMUM_CAPACITY);
            return MAXIMUM_CAPACITY;
        } else {
            // -- 所有低位变成1后，加1即可 ------------ //
            System.out.println("0 <= n < " + MAXIMUM_CAPACITY);
            return n + 1;
        }
    }

    public static void main(String[] args) {
        // 测试负整数
        //loop(-10, 0);

        // 测试大于 MAXIMUM_CAPACITY 的整数
        //loop(0B0100_0000_0000_0000_0000_0000_0000_0001, 0B0100_0000_0000_0000_0000_0000_0000_0100);

        // 测试小于 MAXIMUM_CAPACITY 的自然数
        //loop(0, 1024);

        // 测试 "int n = cap - 1" 和 "不减1" 的差异
        for (int i = 1; i <= 32; i *= 2) {
            System.out.println("tableSizeFor(" + i + ")=" + tableSizeFor(i));
            System.out.println("--------------------------");
        }
    }

    private static void loop(int min, int max) {
        for (int i = min; i < max; i++) {
            System.out.println("tableSizeFor(" + i + ")=" + tableSizeFor(i));
            System.out.println("--------------------------");
        }
    }

    private static void printf(String k, String v) {
        System.out.printf("%-16s%s%s\n", k, " = ", v);
    }
}
