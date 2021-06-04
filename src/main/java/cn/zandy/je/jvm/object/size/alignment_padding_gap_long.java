package cn.zandy.je.jvm.object.size;

import org.openjdk.jol.info.ClassLayout;

public class alignment_padding_gap_long {

    private long l;

    public static void main(String[] args) {
        alignment_padding_gap_long test = new alignment_padding_gap_long();

        System.out.println(ClassLayout.parseInstance(test).toPrintable());
    }

    /* by java 1.8.0_112, org.openjdk.jol:jol-core:0.9
----------------------------------------------------
情况一 :::: -XX:+UseCompressedClassPointers -XX:+UseCompressedOops :::: 不会出现，对象头正好 16 字节

cn.zandy.je.jvm.object.size.alignment_padding_gap_long object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           84 c0 00 f8 (10000100 11000000 00000000 11111000) (-134168444)
     12     4        (alignment/padding gap)
     16     8   long alignment_padding_gap_long.l              0
Instance size: 24 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total
     */
}
