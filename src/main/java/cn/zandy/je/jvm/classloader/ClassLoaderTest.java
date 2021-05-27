package cn.zandy.je.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zandy on 2019/8/27.
 */
public class ClassLoaderTest {

    /**
     * 1.覆盖 ClassLoader 的 findClass 方法(但是还是走"双亲委派"实现的)
     */
    private static class MyClassLoader extends ClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] codes = getByteCodes(name, getClass());
            if (codes == null) {
                return super.loadClass(name);
            }
            return defineClass(name, codes, 0, codes.length);
        }
    }

    /**
     * 2.覆盖 ClassLoader 的 loadClass 方法(即覆盖了"双亲委派"的实现)
     */
    private static class MyClassLoader1 extends ClassLoader {

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            byte[] codes = getByteCodes(name, getClass());
            if (codes == null) {
                return super.loadClass(name);
            }
            return defineClass(name, codes, 0, codes.length);
        }
    }

    /**
     * ClassLoaderTest.class.getName();          // cn.zandy.je.jdk.classloader.ClassLoaderTest
     * ClassLoaderTest.class.getCanonicalName(); // cn.zandy.je.jdk.classloader.ClassLoaderTest
     * ClassLoaderTest.class.getSimpleName();    // ClassLoaderTest
     * ClassLoaderTest.class.getTypeName();      // cn.zandy.je.jdk.classloader.ClassLoaderTest
     */
    public static void main(String[] args)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 1
        MyClassLoader myLoader = new MyClassLoader();
        Object obj = myLoader.loadClass(ClassLoaderTest.class.getName()).newInstance();
        System.out.println(obj instanceof ClassLoaderTest);

        // 2
        MyClassLoader1 myLoader1 = new MyClassLoader1();
        Object obj1 = myLoader1.loadClass(ClassLoaderTest.class.getName()).newInstance();
        System.out.println(obj1 instanceof ClassLoaderTest);
    }

    private static byte[] getByteCodes(String name, Class<?> clazz) throws ClassNotFoundException {
        String relativePath = name.substring(name.lastIndexOf(".") + 1) + ".class";

        InputStream is = clazz.getResourceAsStream(relativePath);
        if (is == null) {
            return null;
        }

        try {
            byte[] codes = new byte[is.available()];
            is.read(codes);
            return codes;
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
}
