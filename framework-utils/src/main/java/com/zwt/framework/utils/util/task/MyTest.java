package com.zwt.framework.utils.util.task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/10/13
 * @since 1.0
 */
public class MyTest implements Runnable{


    @Override
    public void run() {
        //随机假设我们的任务是这样的
        List<String> s=new ArrayList<>();
        String [] strings=new String[]{"A","B","C","D"};
        int l=(int)Math.ceil(Math.random()*strings.length);
        for(int i=0;i<l;i++){
            s.add(strings[i]);
        }
        //获取结果
        System.out.println();
        System.out.print("Thread"+Thread.currentThread().getId()+"随机生成的任务--》");
        for(int i=0;i<s.size();i++){
            System.out.print(s.get(i));
        }

        System.out.println();
        DoTask doTask=new DoTask();
        doTask.doTask(s);
    }


    public static void main(String[] args) {
        MyTest test = new MyTest();
        Thread t1 = new Thread(test);
        Thread t2 = new Thread(test);
        Thread t3 = new Thread(test);
        Thread t4 = new Thread(test);
        Thread t5 = new Thread(test);
        Thread t6 = new Thread(test);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();


    }

}
