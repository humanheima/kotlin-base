package base;

/**
 * Crete by dumingwei on 2019-08-23
 * Desc:
 */
public interface A {

    void test();

    default void testAnother(int params) {
        System.out.println("A testAnother");
    }
}
