import java.util.concurrent.*;

/**
 * @author zwt
 * @detail
 * @date 2018/10/9
 * @since 1.0
 */
public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException,TimeoutException{
        //创建一个线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(()-> {
                //System.out.println("CallableTest");
                TimeUnit.SECONDS.sleep(5);
                return "CallableTest";
        });
        //System.out.println(future.get());
        System.out.println(future.get(4,TimeUnit.SECONDS));
    }
}
