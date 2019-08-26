package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 臭皮匠排序（是一种低效的递归排序算法）
 *
 * 原理：
 * 如果最后一个值小于第一个值，则交换这两个数
 * 如果当前集合元素数量大于等于3：
 * 1.使用臭皮匠排序前2/3的元素
 * 2.使用臭皮匠排序后2/3的元素
 * 3.再次使用臭皮匠排序前2/3的元素
 *
 * 最差的时间复杂度为：O(n^(log 3 / log 1.5))
 * 最差的空间复杂度为： O（n）
 * 可见此算法效率相当的低下，比选择、插入、冒泡排序更差！
 *
 * @date 2019/8/13
 * @since 1.0
 */
public class StoogeSort {
    public static void stoogeSort(int[] array) {
        stoogeSort(array, 0, array.length - 1);
    }
    private static void stoogeSort(int[] array, int low, int high) {
        //如果第一个数大于最后一个数，交换位置
        if (array[low] > array[high]) {
            swap(array, low, high);
        }
        if (low + 1 >= high){
            return;
        }
        int third = (high - low + 1) / 3;
        //排序前2/3数组元素
        stoogeSort(array, low, high - third);
        //排序后2/3数组元素
        stoogeSort(array, low + third, high);
        //排序前2/3数组元素
        stoogeSort(array, low, high - third);
    }
    private static void swap(int[] a, int b, int c) {
        if (b == c){
            return;
        }
        int temp = a[b];
        a[b] = a[c];
        a[c] = temp;
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("StoogeSort排序前：");
        System.out.println(Arrays.toString(a));
        stoogeSort(a);
        System.out.println("StoogeSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
