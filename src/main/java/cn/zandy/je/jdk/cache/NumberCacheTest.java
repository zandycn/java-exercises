package cn.zandy.je.jdk.cache;

/**
 * 原始数据类型【包装类】中的缓存机制.
 *
 * <pre>
 *     包装类都是 final class
 *     包装类中的成员变量 value 也是 final 的
 * ==============================
 *     结合 jdk 8 源码得到的结论
 *     Byte      : ByteCache      -> new Byte[256]           -> [ -128, 127  ]
 *     Short     : ShortCache     -> new Short[256]          -> [ -128, 127  ]
 *     Integer   : IntegerCache   -> new Integer[high+128+1] -> [ -128, high ] 127 <= high <= (Integer.MAX_VALUE-128-1)
 *     Long      : LongCache      -> new Long[256]           -> [ -128, 127  ]
 *     Character : CharacterCache -> new Character[128]      -> [    0, 127  ]
 *     Boolean   : true, false
 *     Float     : 无缓存
 *     Double    : 无缓存
 *
 *     Byte,Short,Integer,Long,Character 的 valueOf 方法：
 *     · 作用是将【原始类型】转换成【包装类】
 *     · 如果入参在缓存的范围内，直接返回【缓存中的对象】；如果超出对应范围，仍然会【去创建新的对象】
 * ==============================
 *     参考：https://www.cnblogs.com/YangJavaer/p/13213003.html
 * </pre>
 */
public class NumberCacheTest {

    public static void main(String[] args) {
        test1();
        //test2();
        //test3();
    }

    private static void test1() {
        Integer i1 = 33, i2 = 33;
        System.out.println(i1 == i2);   // true

        Integer i11 = 333, i22 = 333;
        System.out.println(i11 == i22); // false

        Character c1 = 127, c2 = 127;
        System.out.println(c1 == c2);   // true

        Character c11 = 128, c22 = 128;
        System.out.println(c11 == c22); // false

        Float f1 = 1.0f, f2 = 1.0f;
        System.out.println(f1 == f2);   // false

        Double d1 = 1.2, d2 = 1.2;
        System.out.println(d1 == d2);   // false
    }

    private static void test2() {
        Integer i1 = 40, i2 = 40, i3 = 0;
        Integer i4 = new Integer(40), i5 = new Integer(40), i6 = new Integer(0);

        System.out.println("i1 == i2     " + (i1 == i2));      // true
        System.out.println("i1 == i2+i3  " + (i1 == i2 + i3)); // true
        System.out.println("i1 == i4     " + (i1 == i4));      // false
        System.out.println("i4 == i5     " + (i4 == i5));      // false
        System.out.println("i4 == i5+i6  " + (i4 == i5 + i6)); // true
        System.out.println("i4 == i5+i6  " + (i4.intValue() == i5.intValue() + i6.intValue())); // true
        System.out.println("40 == i5+i6  " + (40 == i5 + i6)); // true
    }

    private static void test3() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Long g = 3L;

        System.out.println(c == (a + b));    // true
        System.out.println(c.equals(a + b)); // true
        System.out.println(g == (a + b));    // true
        System.out.println(g.equals(a + b)); // false
    }
}
