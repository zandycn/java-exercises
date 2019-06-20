package cn.zandy.je.language.specification;

/**
 * Java 运算符优先级测试.
 */
public class PrecedenceOfOperator {

    public static void main(String[] args) {
        // && 运算优先级高于 ||
        test001();
    }

    private static void test001() {
        // true
        System.out.println(true || false && false);

        // true
        System.out.println(true || (false && false));

        // false
        System.out.println((true || false) && false);
    }
}
