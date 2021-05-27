package cn.zandy.je.jvm.object.size;

import org.openjdk.jol.info.ClassLayout;

public class DD extends MyDog {

    private String ddaa;
    private Object ddo;
    private int ddinteger;

    // Test -> -XX:FieldsAllocationStyle=0/1/3
    public static void main(String[] args) {
        DD dd = new DD();
        System.out.println(ClassLayout.parseInstance(dd).toPrintable());
    }
}
