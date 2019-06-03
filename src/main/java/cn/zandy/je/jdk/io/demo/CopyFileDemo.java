package cn.zandy.je.jdk.io.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Created by zandy on 2019/6/3.
 */
public class CopyFileDemo {

    private static void copyBytes(File src, File desc) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(src));
            os = new BufferedOutputStream(new FileOutputStream(desc));

            byte[] bytes = new byte[1024];
            int readCount;
            for (; ; ) {
                readCount = is.read(bytes);
                if (readCount == -1) {
                    break;
                }
                os.write(bytes, 0, readCount);
            }
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
            if (is != null) {
                try {
                    is.close(); // 原则上: 先开启的后关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void copyChars(File src, File desc) {
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             Writer writer = new BufferedWriter(new FileWriter(desc))) {

            /* char[] chars = new char[1024];
            int readCount = 0;
            for (; ; ) {
                readCount = reader.read(chars);
                if (readCount == -1) {
                    break;
                }

                writer.write(chars, 0, readCount);
            } */
            String line;
            for (; ; ) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                writer.write(line);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyDirectory(File src, File dest) throws IOException {
        if ((src == null) || !src.exists()) {
            throw new NullPointerException("源文件或文件夹不存在！");
        } else if (src.isDirectory()) {
            File copySrc = getTarget(src, dest);
            copySrc.mkdirs();

            File[] children = src.listFiles();
            for (File child : children) {
                copyDirectory(child, copySrc);
            }
        } else {
            copyBytes(src, getTarget(src, dest));
        }
    }

    private static File getTarget(File src, File dest) {
        return new File(dest, src.getName());
    }
}
