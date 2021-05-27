package cn.zandy.je.jvm.object.size;

import org.openjdk.jol.info.ClassLayout;

public class alignment_padding_gap_array {

    public static void main(String[] args) {
        Object[] a1 = new Object[2];
        System.out.println(ClassLayout.parseInstance(a1).toPrintable());

        Dog[] a2 = new Dog[3];
        a2[0] = new Dog();
        a2[1] = new Dog();
        System.out.println(ClassLayout.parseInstance(a2).toPrintable());

        int[] a3 = new int[3];
        System.out.println(ClassLayout.parseInstance(a3).toPrintable());
    }

    /* by java 1.8.0_112, org.openjdk.jol:jol-core:0.9
----------------------------------------------------
情况一 :::: -XX:+UseCompressedClassPointers -XX:+UseCompressedOops :::: 不会出现，对象头正好 16 字节

[Ljava.lang.Object; object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           f5 22 00 f8 (11110101 00100010 00000000 11111000) (-134208779)
     12     4                    (object header)                           02 00 00 00 (00000010 00000000 00000000 00000000) (2)
     16     8   java.lang.Object Object;.<elements>                        N/A
Instance size: 24 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

[Lcn.zandy.je.jvm.object.size.Dog; object internals:
 OFFSET  SIZE                              TYPE DESCRIPTION                               VALUE
      0     4                                   (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                   (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                   (object header)                           ce f2 00 f8 (11001110 11110010 00000000 11111000) (-134155570)
     12     4                                   (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     16    12   cn.zandy.je.jvm.object.size.Dog Dog;.<elements>                           N/A
     28     4                                   (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

[I object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           6d 01 00 f8 (01101101 00000001 00000000 11111000) (-134217363)
     12     4        (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     16    12    int [I.<elements>                             N/A
     28     4        (loss due to the next object alignment)
Instance size: 32 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

----------------------------------------------------
情况二 :::: -XX:-UseCompressedClassPointers -XX:+UseCompressedOops :::: 会出现 (alignment/padding gap)

[Ljava.lang.Object; object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           50 51 f7 0c (01010000 01010001 11110111 00001100) (217534800)
     12     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4                    (object header)                           02 00 00 00 (00000010 00000000 00000000 00000000) (2)
     20     4                    (alignment/padding gap)
     24     8   java.lang.Object Object;.<elements>                        N/A
Instance size: 32 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total

[Lcn.zandy.je.jvm.object.size.Dog; object internals:
 OFFSET  SIZE                              TYPE DESCRIPTION                               VALUE
      0     4                                   (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                   (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                   (object header)                           28 a6 4d 0d (00101000 10100110 01001101 00001101) (223192616)
     12     4                                   (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4                                   (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     20     4                                   (alignment/padding gap)
     24    12   cn.zandy.je.jvm.object.size.Dog Dog;.<elements>                           N/A
     36     4                                   (loss due to the next object alignment)
Instance size: 40 bytes
Space losses: 4 bytes internal + 4 bytes external = 8 bytes total

[I object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           68 9b eb 0c (01101000 10011011 11101011 00001100) (216767336)
     12     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4        (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     20     4        (alignment/padding gap)
     24    12    int [I.<elements>                             N/A
     36     4        (loss due to the next object alignment)
Instance size: 40 bytes
Space losses: 4 bytes internal + 4 bytes external = 8 bytes total

----------------------------------------------------
情况三 :::: -XX:-UseCompressedOops :::: 会出现 (alignment/padding gap)

[Ljava.lang.Object; object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           50 51 fc 05 (01010000 01010001 11111100 00000101) (100421968)
     12     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4                    (object header)                           02 00 00 00 (00000010 00000000 00000000 00000000) (2)
     20     4                    (alignment/padding gap)
     24    16   java.lang.Object Object;.<elements>                        N/A
Instance size: 40 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total

[Lcn.zandy.je.jvm.object.size.Dog; object internals:
 OFFSET  SIZE                              TYPE DESCRIPTION                               VALUE
      0     4                                   (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                                   (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                   (object header)                           b8 d8 6b 06 (10111000 11011000 01101011 00000110) (107731128)
     12     4                                   (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4                                   (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     20     4                                   (alignment/padding gap)
     24    24   cn.zandy.je.jvm.object.size.Dog Dog;.<elements>                           N/A
Instance size: 48 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total

[I object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           68 9b f0 05 (01101000 10011011 11110000 00000101) (99654504)
     12     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     4        (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     20     4        (alignment/padding gap)
     24    12    int [I.<elements>                             N/A
     36     4        (loss due to the next object alignment)
Instance size: 40 bytes
Space losses: 4 bytes internal + 4 bytes external = 8 bytes total
     */
}
