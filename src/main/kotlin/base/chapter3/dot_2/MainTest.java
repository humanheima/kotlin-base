package base.chapter3.dot_2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2021/4/6.
 * <p>
 * Desc:
 */
public class MainTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        list.add("1");
        list.add("2");
        list.add("3");

        StringFunctions.joinToStringWithDefaultArgs(list);
        StringFunctions.joinToStringWithDefaultArgs(list,",");
        StringFunctions.joinToStringWithDefaultArgs(list,",","{");
        StringFunctions.joinToStringWithDefaultArgs(list,",","{","}");

    }
}
