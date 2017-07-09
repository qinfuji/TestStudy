package examples;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/7/9.
 */
public class TestDown {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch begin = new CountDownLatch(1);
        begin.countDown();
        begin.await();
    }
}
