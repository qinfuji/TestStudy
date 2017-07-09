package examples;

import io.vertx.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.provider.certpath.Vertex;

import java.util.concurrent.*;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/7/8.
 */
public class Demo2 {

    private Demo1 demo1;

    private Vertx vertx;

    ExecutorService es = Executors.newCachedThreadPool();

    public String hello(String name){
        return this.demo1.hello(name);
    }

    public void setDemo1(Demo1 demo1){
        this.demo1 = demo1;
    }
    public void setVertx(Vertx vertx){
        this.vertx = vertx;
    }

    class Task<T> implements Callable<T>{

        public T message;

        public Task(T message){
            this.message = message;
        }

        public T call() throws InterruptedException {
            Thread.sleep(3000);
            System.out.println("Task message:"+this.message);
            return  this.message;
        }
    }

    public  Future<String> asyncHello(String name){
        Future<String> ft =  es.submit(new Task<String>("hello "+name));
        return ft;
    }

    public io.vertx.core.Future<String> vertxAsyncHello(String name){
        io.vertx.core.Future<String>  ret = io.vertx.core.Future.future();
        this.vertx.executeBlocking((future)->{
            future.complete("hello "+name);
        },(result)->{
            if(result.succeeded()){
                ret.complete((String)result.result());
            }else{
                ret.fail(result.cause());
            }
        });
        return ret;
    }

    public void asyncCallback(String name , Handler<AsyncResult<String>> handler){
        io.vertx.core.Future<String>  ret = io.vertx.core.Future.future();
        this.vertx.executeBlocking((future)->{
            future.complete("hello "+name);
        },(result)->{
            if(result.succeeded()){
                ret.complete((String)result.result());
                handler.handle(io.vertx.core.Future.succeededFuture((String)result.result()));
            }else{
                handler.handle(io.vertx.core.Future.failedFuture(result.cause()));
            }
        });
    }
}
