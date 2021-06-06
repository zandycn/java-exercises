package cn.zandy.je.jdk.cache;

/**
 * <pre>
 *   symbolTable.hpp
 *       // CONSTANT_Utf8 -> Symbol*（一个指针，指向一个 Symbol 类型的 C++ 对象，内容是跟 Class 文件同样格式的 UTF-8 编码的字符串）
 *       class SymbolTable : public RehashableHashtable<Symbol*, mtSymbol> {...};
 *
 *       // CONSTANT_String -> java.lang.String（一个实际的 Java 对象的引用，C++ 类型是 oop）
 *       class StringTable : public RehashableHashtable<oop,     mtSymbol> {...};
 * </pre>
 *
 * 【ldc 指令的说明】
 * 参考：https://www.zhihu.com/question/55994121/answer/408891707
 *
 * 对于一个字符串比如 String s = "abc";:
 *
 * ① 在类加载的过程中，"abc" 最终作为一个 UTF8 的字面量被塞进了符号表 SymbolTable 中，并映射到运行时常量池
 * ② 在指令执行阶段，ldc 指令在将常量 push 进操作数栈之前，会做以下事情：
 * 到当前类的运行时常量池（runtime constant pool, HotSpot VM 里是 ConstantPool + ConstantPoolCache）去查找该 index 对应的项，
 * 如果该项尚未 resolve 则 resolve 之，并返回 resolve 后的内容。
 *
 * 在遇到 String 类型常量时，resolve 的过程如果发现 StringTable 已经有了内容匹配的 java.lang.String 的引用，则直接返回这个引用；
 * 反之，如果 StringTable 里尚未有内容匹配的 String 实例的引用，则会在 Java 堆里创建一个对应内容的 String 对象，
 * 然后在 StringTable 记录下这个引用，并返回这个引用出去。
 *
 * ③ 将返回的引用压栈
 *
 * 【回答之前的疑问】<pre> new String("2") + new String("2"); </pre>
 * Q: 执行完这段代码，为什么在 StringTable 中没有指向 interned string "22" 的引用?
 * A: 因为代码中没有字面量"22", stringBuilder.toString 时，不会使用 ldc 指令，所以只是在堆中创建一个对象
 *
 * ==========================
 * https://www.iteye.com/blog/rednaxelafx-774673 (10年的文章，下面的评论比较"激烈"。我对其中 "ldc指令……"的描述有异议)
 */
public class StringPoolTest {

    //StringBuffer  :: public synchronized String toString() { return new String(toStringCache, true); }
    //StringBuilder :: public String toString()              { return new String(value, 0, count); }
    public static void main(String[] args) {
        //test1();
        test2();
        //test3();
        //test4();
        //test5();
    }

    private static void test1() {
        String s = new String("abc");           // 生成了 2 个对象：interned string 和 new String, 都在堆中. s 指向后者
        // 0 new            - 这里是在堆中创建对象
        // 3 dup
        // 4 ldc #4 <abc>   - 这里是在堆中创建了 interned string 对象
        // invokespecial

        String s1 = "abc";                      //               s1 指向前者
        String s2 = new String("abc").intern(); //               s2 指向前者

        System.out.println(s == s1);  // false
        System.out.println(s == s2);  // false
        System.out.println(s1 == s2); // true
    }

    // 【interned string】和【静态变量】在 Java 7 之前保存在方法区，在 Java 7 之后保存在堆上
    // jdk8
    private static void test2() {
        String str1 = new String("1");
        str1.intern(); // 上面 new String("1") 中， ldc 指令已经调用了 intern; 所以这行是没啥用的了。
        String str2 = "1";
        System.out.println(str1 == str2); // false

        String str3 = new String("2") + new String("2");
        str3.intern();
        String str4 = "22";
        System.out.println(str3 == str4); // true
    }

    // jdk8
    private static void test3() {
        String str1 = new String("1");
        String str2 = "1";
        str1.intern();
        System.out.println(str1 == str2); //false

        String str3 = new String("2") + new String("2");
        String str4 = "22";
        str3.intern();
        System.out.println(str3 == str4); // false
    }

    private static void test4() {
        String str1 = "aaa";
        String str2 = "bbb";
        String str3 = "aaabbb";
        String str4 = str1 + str2;
        String str5 = "aaa" + "bbb";
        System.out.println(str3 == str4);          // false
        System.out.println(str3 == str4.intern()); // true
        System.out.println(str3 == str5);          // true
    }

    // 两个字符串合并，使用 concat
    private static void test5() {
        String str = "abc";
        String str1 = null;
        String str2 = "";

        StringBuilder sb = new StringBuilder();
        sb.append(str).append(str1);
        System.out.println(sb.toString());

        System.out.println(str.concat(str2));
        System.out.println(str == str.concat(str2));
        System.out.println(str.concat(str1)); // NullPointerException
    }

    private static void seeByteCode() {
        String s = new String();
        s += "ab";
        s = s.intern();
        //   0 new #3 <java/lang/String>
        //   3 dup
        //   4 invokespecial #16 <java/lang/String.<init>>
        //   7 astore_0
        //   8 new #10 <java/lang/StringBuilder>
        //  11 dup
        //  12 invokespecial #11 <java/lang/StringBuilder.<init>>
        //  15 aload_0
        //  16 invokevirtual #13 <java/lang/StringBuilder.append>            返回了 this, 压入栈顶
        //  19 ldc #17 <ab>
        //  21 invokevirtual #13 <java/lang/StringBuilder.append>            返回了 this, 压入栈顶
        //  24 invokevirtual #14 <java/lang/StringBuilder.toString>
        //  27 astore_0
        //  28 aload_0
        //  29 invokevirtual #6 <java/lang/String.intern>
        //  32 astore_0
        //  33 return
    }
}
