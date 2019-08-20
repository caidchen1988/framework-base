package com.zwt.framework.utils.util.sort;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zwt
 * @detail SleepSort 睡排序
 *
 * 不稳定
 * 不可靠
 * 时间复杂度远大于其他排序算法
 * 空间复杂度也大于其他排序算法
 *
 * 强烈不推荐使用此方法排序
 * @date 2019/8/14
 * @since 1.0
 */
public class SleepSort {
    public static List<Integer> sleepSort(int[] nums) {
        //创建num个线程池，进行睡排序
        ExecutorService executorService = Executors.newFixedThreadPool(nums.length);
        final CountDownLatch doneSignal = new CountDownLatch(nums.length);
        final List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        List<Future> futureList = new ArrayList<>();
        for (final int num:nums) {
           Future future = executorService.submit(()->{
                doneSignal.countDown();
                try {
                    doneSignal.await();
                    //线程时间为毫秒，可靠性较弱，我们 * 1000 改为秒，可靠性会提高，但是仍有可能排序错误！！！
                    Thread.sleep(num * 1000);
                    list.add(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            futureList.add(future);
        }
        //循环检测线程是否完成
        while (true){
            boolean finish = true;
            for(Future future:futureList){
                if(!future.isDone()){
                    finish = false;
                }
            }
            if(finish){
               executorService.shutdown();
               break;
            }
        }
        return list;
    }
    public static void main(String[] args) {
        int[] a = new int[10];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10);
        }
        System.out.println("SleepSort排序前：");
        System.out.println(Arrays.toString(a));
        List<Integer> list = sleepSort(a);
        System.out.println("SleepSort排序后：");
        System.out.println(Arrays.toString(list.toArray()));
    }
}
