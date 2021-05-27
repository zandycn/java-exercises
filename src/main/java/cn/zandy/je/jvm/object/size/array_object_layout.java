package cn.zandy.je.jvm.object.size;

import org.openjdk.jol.info.ClassLayout;

public class array_object_layout {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    // by java 1.8.0_112, org.openjdk.jol:jol-core:0.9

    static void test1() {
        byte[] b1 = new byte[1024];
        Byte[] b2 = new Byte[1024];

        System.out.println(ClassLayout.parseInstance(b1).toPrintable());
        System.out.println(ClassLayout.parseInstance(b2).toPrintable());

        // -XX:+UseCompressedOops
        // header   : 8,4,4  =   16        header   : 8,4,4  =   16
        // ins data : 1024*1 = 1024        ins data : 1024*4 = 4096
        // padding  :             0        padding  :             0
        // total    :          1040        total    :          4112

        // -XX:-UseCompressedClassPointers -XX:+UseCompressedOops
        // header   : 8,8,4  =   20        header   : 8,8,4  =   20
        // ins data : 1024*1 = 1024        ins data : 1024*4 = 4096
        // padding  :             4        padding  :             4
        // total    :          1048        total    :          4120

        // -XX:-UseCompressedOops
        // header   : 8,8,4  =   20        header   : 8,8,4  =   20
        // ins data : 1024*1 = 1024        ins data : 1024*8 = 8192
        // padding  :             4        padding  :             4
        // total    :          1048        total    :          8216

        // 如果不用 jol, 这种方式也可以
        //System.out.println("size(s):" + org.apache.lucene.util.RamUsageEstimator.sizeOf(new byte[1024]));
        //System.out.println("size(s):" + org.apache.lucene.util.RamUsageEstimator.shallowSizeOf(new Byte[1024]));
    }

    // 测试: "根据指针压缩的影响可以想到，开启指针压缩时，new long[1024] 比 new Long[1024] 占用的空间要大"
    // 占用堆内存空间？
    static void test2() {
        long[] l1 = new long[1024];
        Long[] l2 = new Long[1024];

        System.out.println(ClassLayout.parseInstance(l1).toPrintable());
        System.out.println(ClassLayout.parseInstance(l2).toPrintable());

        // -XX:+UseCompressedOops
        // header   : 8,4,4  =   16        header   : 8,4,4  =   16
        // ins data : 1024*8 = 8192        ins data : 1024*4 = 4096
        // padding  :             0        padding  :             0
        // total    :          8208        total    :          4112
    }
}
