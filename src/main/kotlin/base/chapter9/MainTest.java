package base.chapter9;

/**
 * Created by dumingwei on 2020/10/16.
 * <p>
 * Desc:
 */
public class MainTest {

    public static void main(String[] args) {
        MainTest mainTest = new MainTest();
        mainTest.sum(2);
    }

    public <T extends Number> void sum(T num) {

    }

    void demo(Source<Orange> oranges) {
        Source<? extends Fruit> fruits = oranges;

        fruits.nextT().base();
    }

}

class Fruit {

    void base() {

    }
}

class Orange extends Fruit {
    void child() {

    }
}

interface Source<T> {
    T nextT();
}

