package cn.zandy.je.jdk.io.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 输出流(字符)
 * 注意：字符流只能处理存文本文件
 *
 * @author zandy
 */
public class FileWriterDemo {

    public static void main(String[] args) {
        File dest = new File("/Users/zandy/testf.txt");

        Writer writer = null;
        try {
            // 第二个参数表示 是否是向目标追加内容，默认为false(不追加)
            writer = new FileWriter(dest, true);
            writer.write("Hello World! 你好，世界！哈哈\r\n".toCharArray());
            writer.append("附加一些内容\r\n------------");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
