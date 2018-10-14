package com.zwt.framework.utils.util.java8.test6;

import java.util.concurrent.RecursiveTask;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

    //要求和的数组
    private final int[] numbers;
    //子任务处理的数组的起始位置
    private final int start;
    //子任务处理的数组的终止位置
    private final int end;

    //不再将任务划分的子任务数组大小
    public static final long THRESHOLD=10000;

    public ForkJoinSumCalculator(int[] numbers){
        this(numbers,0,numbers.length);
    }
    private ForkJoinSumCalculator(int[] numbers,int start,int end){
        this.numbers=numbers;
        this.start=start;
        this.end=end;
    }

    @Override
    protected Long compute() {
        int length=end-start;
        //小于等于阈值，计算结果
        if(length<=THRESHOLD){
            return computeSequentially();
        }
        //创建一个子任务来为数组的前一半求和
        ForkJoinSumCalculator leftTask=new ForkJoinSumCalculator(numbers,start,start+length/2);
        //利用另一个ForkJoinPool线程异步执行新创建的子任务
        leftTask.fork();
        //创建一个任务为数组的后一半求和
        ForkJoinSumCalculator rightTask=new ForkJoinSumCalculator(numbers,start+length/2,end);
        //同步执行第二个子任务，有可能允许进一步递归划分
        Long rightResult=rightTask.compute();
        //读取第一个子任务的结果，没有完成就等待
        Long leftResult=leftTask.join();
        //合并结果
        return rightResult+leftResult;
    }

    //子任务数组求和
    private long computeSequentially(){
        long sum=0;
        for(int i=start;i<end;i++){
            sum+=numbers[i];
        }
        return sum;
    }
}
