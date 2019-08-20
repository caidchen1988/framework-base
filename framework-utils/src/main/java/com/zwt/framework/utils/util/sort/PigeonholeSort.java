package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 鸽巢排序（PigeonholeSort）
 * 原理类似桶排序,同样需要一个很大的鸽巢[桶排序里管这个叫桶,名字无所谓了]
 * 鸽巢其实就是数组,数组的索引位置就表示值,该索引位置的值表示出现次数,如果全部为1次或0次那就是桶排序
 *
 * 1.给定一个待排序数组，创建一个备用数组（鸽巢），并初始化元素为0，备用数组的索引即是待排序数组的值。
 * 2.把待排序数组的值，放到“鸽巢”里（即用作备用数组的索引）。
 * 3.把鸽巢里的值再依次送回待排序数组。
 *
 * 最坏时间复杂度： O(N+n)
 * 最好时间复杂度：O(N+n)
 * 平均时间复杂度： O(N+n)
 * 最坏空间复杂度：O(N*n)
 *
 * 稳定性  稳定
 * @date 2019/8/14
 * @since 1.0
 */
public class PigeonholeSort {
    private static int[] pigeonhole(int[] array, int maxNumber){
        int[] pigeonhole = new int[maxNumber + 1];
        /*
         * pigeonhole[10] = 4; 的含意是
         * 在待排数组中有4个10出现,同理其它
         */
        for (int item:array) {
            pigeonhole[item]++;
        }
        return pigeonhole;
    }
    public static void pigeonholeSort(int[] array){
        int max = array[0];
        for (int e:array) {
            if(e>max){
                max = e;
            }
        }
        //构建鸽巢
        int[] sorted = pigeonhole(array,max);
        int index = 0;
        for(int i=0;i<sorted.length;i++){
            //如果有不为空的说明放入了元素，下标即为元素值
            if(sorted[i]!=0){
                for(int k=0;k<sorted[i];k++){
                    array[index] = i;
                    index++;
                }
            }
        }
    }
    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("PigeonholeSort排序前：");
        System.out.println(Arrays.toString(a));
        pigeonholeSort(a);
        System.out.println("PigeonholeSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
