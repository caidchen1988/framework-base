package com.zwt.framework.utils.util.arrayFilter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zwt
 * @detail
 * @date 2018/11/5
 * @since 1.0
 */
public class ArrayFilterToNewArray {
    public static void main(String[] args) {
        Integer [] ints=new Integer[] {1,3,5,2,2,7,9,2,3,4,5,6,7,6,6,6,6,6,6,66,9};

        Integer [] s=getArray6(ints,i->i%2!=0,Integer.class);
        for(Integer i:s){
            System.out.println(i);
        }
    }

    /**
     * 过滤形成新的数组（转换为List，通过List里面remove(安全的)移除元素）
     * @param sourceArray
     * @param predicate
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] getArray1(T[] sourceArray,Predicate<T> predicate,Class<T> type){
        //asList生成的是一个List的内部类，无法调用add remove delete方法会抛出异常
        List<T> sourceList= new ArrayList<>(Arrays.asList(sourceArray));
        //使用Java8 函数式接口，移除不符合条件的元素
        sourceList.removeIf(predicate);
        return sourceList.toArray((T[]) Array.newInstance(type,0));
    }


    /**
     * 过滤形成新的数组（两次循环查找符合条件的，移动过去）
     * @param sourceArray
     * @param predicate
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] getArray2(T[] sourceArray,Predicate<T> predicate,Class<T> type){
        int count=0;
        for (T t:sourceArray){
            if(predicate.test(t)){
                count++;
            }
        }
        //都不符合条件
        if(count==0){
            return Arrays.copyOf(sourceArray,sourceArray.length);
        }
        //都符合条件
        if(count==sourceArray.length){
            return (T[]) Array.newInstance(type,0);
        }

        T [] targetArray=(T[]) Array.newInstance(type,sourceArray.length-count);
        int index=0;
        for (T t:sourceArray){
            if(!predicate.test(t)){
               targetArray[index]=t;
               index++;
            }
        }
        return targetArray;
    }

    /**
     * 对比第二种方法，这属于空间换时间的做法
     * @param sourceArray
     * @param predicate
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] getArray3(T[] sourceArray,Predicate<T> predicate,Class<T> type){
        //直接创建一个空的一样长的数组
        T[] tempArray=(T[]) Array.newInstance(type,sourceArray.length);
        //不符合条件的数量
        int count=0;
        for (T t:sourceArray){
            //拿到不符合过滤条件的，一个个赋值给新数组
            if(!predicate.test(t)){
                tempArray[count]=t;
                count++;
            }
        }
        //最后这个数组长度<=原数组长度
        //两个特殊处理下
        if(count==0){
            return (T[]) Array.newInstance(type,0);
        }
        return Arrays.copyOf(tempArray,count);
    }

    /**
     * 借助list拿到符合条件的，在强转成数组
     * @param sourceArray
     * @param predicate
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] getArray4(T[] sourceArray,Predicate<T> predicate,Class<T> type){
        //记录符合条件的元素下标
        List<T> targetList=new ArrayList<>();
        for (T t:sourceArray){
            if(!predicate.test(t)){
                targetList.add(t);
            }
        }
        return targetList.toArray((T[]) Array.newInstance(type,0));
    }

    /**
     * Java8 串行流语法(收集符合条件的)
     * @param sourceArray
     * @param predicate
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] getArray5(T[] sourceArray,Predicate<T> predicate,Class<T> type){
        return Arrays.stream(sourceArray).sequential().
                filter(predicate).
                collect(Collectors.toList()).
                toArray((T[]) Array.newInstance(type,0));
    }
    /**
     * Java8 并行流语法(收集符合条件的)
     * @param sourceArray
     * @param predicate
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] getArray6(T[] sourceArray,Predicate<T> predicate,Class<T> type){
        return Arrays.stream(sourceArray).parallel().
                filter(predicate).
                collect(Collectors.toList()).
                toArray((T[]) Array.newInstance(type,0));
    }


}
