package examples;


import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

@Test
@ContextConfiguration(locations = {"classpath:spring-test-config.xml"})
public class Test1 extends AbstractTestNGSpringContextTests {


    @Autowired
    private Demo1 demo1;
    @Autowired
    private Demo2 demo2;

//    @Autowired
//    private Vertx vertx;


    @Test(dataProvider = "getHelloRet", groups = {"m1"})
    public void testMethod1(String name, String expected) {
        when(demo1.hello(name)).thenReturn(expected);
        demo2.setDemo1(demo1);
        String ret = demo2.hello(name);
        Assert.assertEquals(ret, expected);
        verify(this.demo1).hello(name);
    }


    @DataProvider(name = "getHelloRet")
    private Object[][] getHelloRet() {
        return new Object[][]{{"qinfuji", "hello qinfuji"}};
    }

    @Test
    public void testMethod2() {
    }

    @Test
    public void testMethod3() {
    }

    @Test
    public void testAsyncMethod(){
         Future<String> helloMessage =  demo2.asyncHello("qinfuji");
         System.out.println(helloMessage);
        try {
           String helloMessageV =  FutureWait.waitFuture(helloMessage);
           System.out.println(helloMessageV);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void  vertxAsyncHello(){
        demo2.setVertx(Vertx.vertx());
        io.vertx.core.Future<String> helloMessage =  demo2.vertxAsyncHello("qinfuji");
        System.out.println(helloMessage);
        try {
            String helloMessageV =  FutureWait.waitFuture(helloMessage);
            System.out.println("vertxAsyncHello :" +helloMessageV);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test(timeOut=500)
    public void  asyncCallback(){
        demo2.setVertx(Vertx.vertx());
        FutureWait.SyncResultHander handler = new FutureWait.SyncResultHander<String , AsyncResult<String>>();
        demo2.asyncCallback("qinfuji" ,handler);
        try {
            String helloMessage =  (String)handler.get();
            System.out.println("asyncCallback : "+helloMessage);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
