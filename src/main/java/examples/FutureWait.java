package examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/7/9.
 */
public  class FutureWait {

    private static class RetValue {
        public Object ret;
    }

    public static <T> T waitReturt(final Future<T> future) throws Throwable {
        if (future == null) {
            return null;
        }
        final CountDownLatch begin = new CountDownLatch(1);
        final RetValue rv = new RetValue();
        new Thread() {
            public void run() {
                try {
                    Object ret = future.get();
                    rv.ret = ret;
                } catch (Throwable e) {
                    rv.ret = e;
                }
                begin.countDown();
            }
        }.start();
        begin.await();
        Object ret = rv.ret;
        if (ret instanceof Exception) {
            throw (Throwable) ret;
        } else {
            return (T) ret;
        }
    }

    public static <T> T waitReturt(final io.vertx.core.Future<T> future) throws Throwable {
        if (future == null) {
            return null;
        }
        final CountDownLatch begin = new CountDownLatch(1);
        final RetValue rv = new RetValue();
        new Thread() {
            public void run() {
                try {
                    future.setHandler(result->{
                         if(result.succeeded()){
                             rv.ret = result.result();
                         }else{
                             rv.ret = result.result();
                         }
                        begin.countDown();
                    });
                } catch (Throwable e) {
                    rv.ret = e;
                    begin.countDown();
                }

            }
        }.start();
        begin.await();
        Object ret = rv.ret;
        if (ret instanceof Exception) {
            throw (Throwable) ret;
        } else {
            return (T) ret;
        }
    }
}
