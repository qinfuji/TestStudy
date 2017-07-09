package examples;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/7/9.
 */
public class FutureWait {

    private static class RetValue<T> {
        public T ret;
        public Throwable err;
    }

    public static class SyncResultHander<T, E extends AsyncResult<T>> implements Handler<E> {
        private T retValue = null;
        private Throwable err = null;
        private final CountDownLatch begin = new CountDownLatch(1);

        @Override
        public void handle(E result) {
            try {
                if (result.succeeded()) {
                    retValue = result.result();
                } else {
                    err = result.cause();
                }
            } catch (Exception e) {
                err = e;
            }
            begin.countDown();
        }

        public T get() throws Throwable {
            begin.await();
            if (err != null) {
                throw err;
            }
            return retValue;
        }
    }

    public static <T> T waitFuture(final Future<T> future) throws Throwable {
        if (future == null) {
            return null;
        }
        final CountDownLatch begin = new CountDownLatch(1);
        final RetValue<T> rv = new RetValue<T>();
        new Thread() {
            public void run() {
                try {
                    rv.ret = future.get();
                } catch (Throwable e) {
                    rv.err = e;
                }
                begin.countDown();
            }
        }.start();
        begin.await();
        if (rv.err != null) {
            throw rv.err;
        }
        return rv.ret;
    }

    public static <T> T waitFuture(final io.vertx.core.Future<T> future) throws Throwable {
        if (future == null) {
            return null;
        }
        final CountDownLatch begin = new CountDownLatch(1);
        final RetValue<T> rv = new RetValue<T>();

        try {
            future.setHandler(result -> {
                if (result.succeeded()) {
                    rv.ret = result.result();
                } else {
                    rv.err = result.cause();
                }
                begin.countDown();
            });
        } catch (Throwable e) {
            rv.err = e;
            begin.countDown();
        }
        begin.await();
        if (rv.err != null) {
            throw rv.err;
        }
        return rv.ret;
    }
}
