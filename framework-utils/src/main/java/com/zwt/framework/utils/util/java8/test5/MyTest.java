package com.zwt.framework.utils.util.java8.test5;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class MyTest {
    //并行流计算从1到n的和
    public static long parallelSum(long n){
        return LongStream.rangeClosed(1,n).parallel().reduce(0L,Long::sum);
    }
    //串行计算从1到n的和
    public static long sequentialSum(long n){
        return LongStream.rangeClosed(1,n).reduce(0L,Long::sum);
    }

    public static void main(String[] args) {

    }
}
