package cn.zandy.je.jvm.object.create;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 对象创建的几种方式.
 */
public class Test {

    public static void main(String[] args) {
        try {
            Object o = Object.class.newInstance();
            //Obj o = Obj.class.newInstance();   // IllegalAccessException
            //Obj1 o = Obj1.class.newInstance(); // InstantiationException
            System.out.println(o);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            //Constructor<Obj> constructor =Obj.class.getConstructor(); // NoSuchMethodException
            Constructor<Obj> constructor = Obj.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Obj o = constructor.newInstance();
            System.out.println(o);

            Constructor<Obj1> constructor1 = Obj1.class.getDeclaredConstructor(int.class);
            Obj1 o1 = constructor1.newInstance(99);
            System.out.println(o1);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        TestClone tc = new TestClone();
        Object tc1 = null;
        try {
            tc1 = tc.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println(tc);
        System.out.println(tc1);
    }
}

class Obj {

    private Obj() {
    }
}

class Obj1 {

    private int i;

    public Obj1(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return super.toString() + ":::i=" + i;
    }
}

class TestClone
    //implements Cloneable
{

    @Override
    public Object clone() throws CloneNotSupportedException {
        // 如果此类不实现 Cloneable, 调用 object.clone() 时会抛出 CloneNotSupportedException
        return super.clone();
    }
}
