package cn.zandy.je.jdk.cache;

/**
 * https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html
 *
 * ==========================
 * https://www.zhihu.com/question/64726158
 * https://www.zhihu.com/question/60778124/answer/180076902
 * 【ldc 指令的说明】
 * 对于一个字符串比如 String s = "abc";:
 * ① 在类加载的【加载】阶段，"abc" 最终作为一个 UTF8 的字面量被塞进了符号表 SymbolTable 中，并映射到运行时常量池
 * ② 在指令执行阶段，ldc 指令在将常量 push 进操作数栈之前，会做以下事情：
 * ·  - 1.根据 ldc 的参数 (CONSTANT_String_info 类型) 去运行时常量池 SymbolTable 中找到对应的 tag 的 Symbol 对象
 * ·  - 2.通过 StringTable::intern 将 Symbol 转换成 java_lang_String 类型的字符串对象
 * ·  - 3.然后查看 StringTable 中有没有该字符串
 * ·      - 3.1.如果有，将字符串对象直接返回
 * ·      - 3.2.如果没有，将字符串对象先加入到 StringTable 中，然后返回该对象
 * ③ 将返回的字符串对象压栈
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
    }

    private static void test1() {
        String s = new String("abc");           // 生成了 2 个对象：字符串常量池中的 "abc" 和 堆中对象. s 指向后者
        // 0 new
        // 3 dup
        // 4 ldc #4 <abc>   - 这里会生成 字符串常量池中的 "abc" 对象
        // invokespecial    - 这里是生成 堆中对象

        String s1 = "abc";                      // 相当于 new String("abc").intern();             s1 指向前者
        String s2 = new String("abc").intern(); //                                               s2 指向前者

        System.out.println(s == s1);  // false
        System.out.println(s == s2);  // false
        System.out.println(s1 == s2); // true
    }

    // 运行时常量池一直在方法区 (Method Area), 里面包含了每一个.class文件中的常量池中的内容
    // 而字符串常量池在 Java 7 之前保存在方法区，在 Java 7 之后保存在堆上
    private static void test2() {
        String str1 = new String("1");
        str1.intern();
        String str2 = "1";
        System.out.println(str1 == str2); // false

        // 注 *** 以下注释是针对 Java 7 之后的说明 ***
        String str3 = new String("2") + new String("2"); // 最终是 2 个对象：字符串常量池中的 "2" 和 堆中的字符串对象(value=22).  str3 指向后者
        str3.intern();        // 字符串常量池中没有 "22", 将堆中的字符串对象(value=22)的引用也就是 str3 放到了字符串常量池中
        String str4 = "22";   // str4 指向字符串常量池中的引用，即最终是指向堆中的字符串对象(value=22)
        System.out.println(str3 == str4); // true
    }

    private static void test3() {
        String str1 = new String("1");
        String str2 = "1";
        str1.intern();
        System.out.println(str1 == str2); //false

        String str3 = new String("2") + new String("2");
        String str4 = "22"; // 字符串常量池中没有 "22", 这时在字符串常量池中创建一个 "22" 对象
        str3.intern();      // 已经有了，什么也不干
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
