package cn.zandy.je.jvm.object.size;

import org.openjdk.jol.info.ClassLayout;

public class alignment_padding_gap_7 {

    private int intVal;
    private int intVal1;
    private boolean boolVal;
    private String strVal;

    private alignment_padding_gap_7() {
    }

    public static void main(String[] args) {
        alignment_padding_gap_7 obj = new alignment_padding_gap_7();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }

    /* by java 1.8.0_112, org.openjdk.jol:jol-core:0.9
---------------------------------------
情况一
-XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops

cn.zandy.je.jvm.object.size.alignment_padding_gap_7 object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           84 c0 00 f8 (10000100 11000000 00000000 11111000) (-134168444)
     12     4                int alignment_padding_gap_7.intVal            0
     16     4                int alignment_padding_gap_7.intVal1           0
     20     1            boolean alignment_padding_gap_7.boolVal           false
     21     3                    (alignment/padding gap)
     24     4   java.lang.String alignment_padding_gap_7.strVal            null
     28     4                    (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 3 bytes internal + 4 bytes external = 7 bytes total

---------------------------------------
情况二
-XX:+PrintCommandLineFlags -XX:-UseCompressedClassPointers -XX:+UseCompressedOops

cn.zandy.je.jvm.object.size.alignment_padding_gap_7 object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           30 b8 90 0b (00110000 10111000 10010000 00001011) (194033712)
     12     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4                int alignment_padding_gap_7.intVal            0
     20     4                int alignment_padding_gap_7.intVal1           0
     24     1            boolean alignment_padding_gap_7.boolVal           false
     25     3                    (alignment/padding gap)
     28     4   java.lang.String alignment_padding_gap_7.strVal            null
Instance size: 32 bytes
Space losses: 3 bytes internal + 0 bytes external = 3 bytes total

---------------------------------------
情况三
-XX:+PrintCommandLineFlags -XX:-UseCompressedOops

cn.zandy.je.jvm.object.size.alignment_padding_gap_7 object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           30 b8 2b 0f (00110000 10111000 00101011 00001111) (254523440)
     12     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4                int alignment_padding_gap_7.intVal            0
     20     4                int alignment_padding_gap_7.intVal1           0
     24     1            boolean alignment_padding_gap_7.boolVal           false
     25     7                    (alignment/padding gap)
     32     8   java.lang.String alignment_padding_gap_7.strVal            null
Instance size: 40 bytes
Space losses: 7 bytes internal + 0 bytes external = 7 bytes total
     */
}
