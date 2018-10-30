package com.zwt.framework.utils.util.task.one;

/**
 * @author zwt
 * @detail
 * @date 2018/10/13
 * @since 1.0
 */
public class ThingsMethod {
    public boolean doSomethingA(String id){
        System.out.println("线程"+id+"做A事情开始----->");

        //假设做A事情花费1s
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("线程"+id+"做A事情结束----->");
        return true;
    }
    public boolean doSomethingB(String id){
        System.out.println("线程"+id+"做B事情开始----->");

        //假设做B事情花费3s
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("线程"+id+"做B事情结束----->");
        return true;
    }
    public boolean doSomethingC(String id){
        System.out.println("线程"+id+"做C事情开始----->");

        //假设做C事情花费2s
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("线程"+id+"做C事情结束----->");
        return true;
    }
    public boolean doSomethingD(String id){
        System.out.println("线程"+id+"做D事情开始----->");

        //假设做D事情花费5s
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("线程"+id+"做D事情结束----->");
        return true;
    }
}
