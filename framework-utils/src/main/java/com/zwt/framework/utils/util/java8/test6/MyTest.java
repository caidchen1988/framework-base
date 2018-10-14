package com.zwt.framework.utils.util.java8.test6;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class MyTest {
    public static long forkJoinSum(int [] numbers){
        ForkJoinTask<Long> task=new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
    }

    public static Long sum(int [] numbers){
        long result=0L;
        for(int i=0;i<numbers.length;i++){
            result+=numbers[i];
        }
        return result;
    }

    public static void main(String[] args) {
        //构建一个数组
        int [] numbers=new int[100000000];
        for(int i=0;i<numbers.length;i++){
            numbers[i]=(int)(Math.random() * Integer.MAX_VALUE);
        }

        //分支/合并框架计算执行速度
        long fastest=Long.MAX_VALUE;
        for(int i=0;i<10;i++){
            long start=System.nanoTime();
            forkJoinSum(numbers);
            long duration=(System.nanoTime()-start);
            if(duration<fastest){
                fastest=duration;
            }
        }
        System.out.println("分支/合并最快执行速度为"+fastest+"ns");


        //普通方法计算执行速度
        long fastest1=Long.MAX_VALUE;
        for(int i=0;i<10;i++){
            long start=System.nanoTime();
            sum(numbers);
            long duration=(System.nanoTime()-start);
            if(duration<fastest1){
                fastest1=duration;
            }
        }
        System.out.println("普通算法最快执行速度为"+fastest1+"ns");

        //Stream API 串行方法计算执行速度
        long fastest2=Long.MAX_VALUE;
        for(int i=0;i<10;i++){
            long start=System.nanoTime();
            //sum(numbers);
            Arrays.stream(numbers).sum();
            long duration=(System.nanoTime()-start);
            if(duration<fastest2){
                fastest2=duration;
            }
        }
        System.out.println("Stream API 串行 最快执行速度为"+fastest2+"ns");


        //Stream API 并行方法计算执行速度
        long fastest3=Long.MAX_VALUE;
        for(int i=0;i<10;i++){
            long start=System.nanoTime();
            //sum(numbers);
            Arrays.stream(numbers).parallel().sum();
            long duration=(System.nanoTime()-start);
            if(duration<fastest3){
                fastest3=duration;
            }
        }
        System.out.println("Stream API 并行 最快执行速度为"+fastest3+"ns");
    }
}
