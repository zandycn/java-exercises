package cn.zandy.je.algorithm;

/**
 * Created by zandy on 2019/6/3.
 */
public class FindLarger2N {

    public static void main(String[] args) {
        int MAXIMUM_CAPACITY = 1 << 30;

        for (int i = 0; i <= 32; i++) {
            // HashMap tableSizeFor(int cap)
            int n = i - 1;
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            int result = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
            System.out.println("After tableSizeFor |" + i + "|, result=|" + result + "|");
        }
    }
}
