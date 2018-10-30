package com.zwt.framework.utils.util.task.two;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zwt
 * @detail
 * @date 2018/10/18
 * @since 1.0
 */
public class DoTask {
    private static ThingsMethod method=new ThingsMethod();

    public void doTask(List<String> list){

        for(String taskStr:list){
            ExecutorService executor= Executors.newCachedThreadPool();
            executor.execute(()->{
                final String id="Thread"+Thread.currentThread().getId();
                doSomething(taskStr,id,method);
            });

        }
    }

    public void doSomething(String taskStr,String id,ThingsMethod method){
        switch (taskStr){
            case "A":
                method.doSomethingA(id);
                break;
            case "B":
                method.doSomethingA(id);
                break;
            case "C":
                method.doSomethingA(id);
                break;
            case "D":
                method.doSomethingA(id);
                break;
            default:
                break;
        }
    }
}
