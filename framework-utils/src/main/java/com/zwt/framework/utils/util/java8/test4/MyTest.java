package com.zwt.framework.utils.util.java8.test4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class MyTest {

    //判断质数
    public static boolean isPrime(int candidate){
        int candidateRoot=(int)Math.sqrt((double)candidate);
        return IntStream.rangeClosed(2,candidateRoot).noneMatch(i->candidate%i==0);
    }

    //区分质数和非质数
    public static Map<Boolean,List<Integer>> partitionPrimes(int n){
        return IntStream.rangeClosed(2,n).boxed().collect(Collectors.partitioningBy(candidate->isPrime(candidate)));
    }

    //自定义收集器区分质数非质数
    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n){
        return IntStream.rangeClosed(2,n).boxed().collect(new PrimeNumbersCollector());
    }

    //重写collect收集器
    public static Map<Boolean,List<Integer>> partitionPrimesWithCollector(int n){
        return IntStream.rangeClosed(2,n).boxed().collect(
                ()->new HashMap<Boolean, List<Integer>>(){{
                    put(true,new ArrayList<>());
                    put(false,new ArrayList<>());
                }},(acc,candidate)->{
                    acc.get(PrimeNumbersCollector.isPrime(acc.get(true),candidate)).add(candidate);
                },(map1,map2)->{
                    map1.get(true).addAll(map2.get(true));
                    map1.get(false).addAll(map2.get(false));
                }
        );
    }


    public static void main(String[] args) {
        long fastest=Long.MAX_VALUE;
        for(int i=0;i<10;i++){
            long start=System.nanoTime();
            partitionPrimesWithCustomCollector(1000000);
            long duration=(System.nanoTime()-start)/1000000;
            if(duration<fastest){
                fastest=duration;
            }
        }

        System.out.println("最快执行速度为"+fastest+"ms");
    }
}
