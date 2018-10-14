package com.zwt.framework.utils.util.task;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zwt
 * @detail
 * @date 2018/10/13
 * @since 1.0
 */
public class DoTask {

    private static Map<String,ExecutorService> poolMap=new HashMap<>();


    static{
        //获取指定class的public方法
        Method[] methods=ThingsMethod.class.getDeclaredMethods();
        //有多少个方法就创建多少个线程池,一个方法指定一个定长线程池
        for(Method method:methods){
            ExecutorService pool= Executors.newFixedThreadPool(1);
            poolMap.put(method.getName(),pool);
        }
    }

    //list里面假设是要执行的任务，变化的
    public void doTask(List<String> list){
        for(String taskStr:list){
            final String id="Thread"+Thread.currentThread().getId();
            switch (taskStr){
                case "A":
                    try{
                        executeMethod("doSomethingA",id);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;

                case "B":
                    try{
                        executeMethod("doSomethingB",id);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "C":
                    try{
                        executeMethod("doSomethingC",id);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "D":
                    try{
                        executeMethod("doSomethingD",id);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public void executeMethod(String methodName,String id){
        poolMap.get(methodName).execute(()->{
            try{
                Method method=ThingsMethod.class.getMethod(methodName,String.class);
                method.invoke(ThingsMethod.class.newInstance(),id);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }
}
