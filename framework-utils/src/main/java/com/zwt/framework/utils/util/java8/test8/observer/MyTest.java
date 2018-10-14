package com.zwt.framework.utils.util.java8.test8.observer;

/**
 * @author zwt
 * @detail
 * @date 2018/10/11
 * @since 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        Feed f=new Feed();
        f.registerObserver((String tweet)->{
            if(tweet!=null&&tweet.contains("money")){
                System.out.println("money--money--money");
            }
        });
        f.registerObserver((String tweet)->{
            if(tweet!=null&&tweet.contains("work")){
                System.out.println("work--work--work");
            }
        });
        f.registerObserver((String tweet)->{
            if(tweet!=null&&tweet.contains("other")){
                System.out.println("other--other--other");
            }
        });

        f.notifyObservers("money->work->other");
    }
}
