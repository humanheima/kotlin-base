package handbook.two;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * Created by dmw on 2019/1/3.
 * Desc:
 */
public class MainTest {
    public static void main(String[] args) {

        Observable.just(1)
                .compose(RxJavaUtils.preventDuplicateClicksTransformer(1000, TimeUnit.MILLISECONDS))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
        Observable.just(1)
                .compose(RxJavaUtils.preventDuplicateClicksTransformer(2000, TimeUnit.MILLISECONDS))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
    }
}
