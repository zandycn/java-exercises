package cn.zandy.je.jdk.io.demo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * 输入流(字符)
 * 注意：字符流只能处理存文本文件
 *
 * @author zandy
 */
public class FileReaderDemo {

    public static void main(String[] args) {
        File src = new File("/Users/zandy/zdy.txt"); // 一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾？！。

        Reader reader = null;
        try {
            reader = new FileReader(src);
            char[] c = new char[10];
            int readCount = 0;
            while ((readCount = reader.read(c)) != -1) {
                System.out.println("字符数组(" + readCount + "):" + Arrays.toString(c));
                System.out.println("[" + new String(c) + "]");               // error
                System.out.println("[" + new String(c, 0, readCount) + "]"); // right
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
