package cn.zandy.je.jdk.io.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 输出流(字节)
 *
 * @author zandy
 */
public class FileOutputStreamDemo {

    public static void main(String[] args) {
        File dest = new File("/Users/zandy/testf.txt");

        OutputStream os = null;
        try {
            // 第二个参数表示 是否是向目标追加内容，默认为false(不追加)
            os = new FileOutputStream(dest, true);
            byte[] bytes = "hello world!\r\n".getBytes("UTF-8");
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
