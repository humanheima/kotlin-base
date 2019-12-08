package javatest;

/**
 * Created by dmw on 2018/11/15.
 * Desc:
 */
public class JavaTest implements A, B {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        JavaTest test = new JavaTest();
        test.test();
        test.testAnother(1);

        A a = new JavaTest();
        a.test();

        B b = new JavaTest();
        b.test();
    }

    @Override
    public void test() {
        System.out.println("test");
    }

    @Override
    public void testAnother(int params) {
        A.super.testAnother(params);
        B.super.testAnother(params);
    }
}



