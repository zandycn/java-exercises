package cn.zandy.je.jdk.io.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 输入流(字节)
 *
 * @author zandy
 */
public class FileInputStreamDemo {

    public static void main(String[] args) {
        File src = new File("/Users/zandy/testf.txt"); // abcdefghij0123456789over

        InputStream is = null;
        try {
            is = new FileInputStream(src);
            byte[] b = new byte[10];
            int readCount = 0;
            while ((readCount = is.read(b)) != -1) {
                System.out.println("字节数组(" + readCount + "):" + Arrays.toString(b));
                System.out.println("[" + new String(b, "UTF-8") + "]");               // error
                System.out.println("[" + new String(b, 0, readCount, "UTF-8") + "]"); // right
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
