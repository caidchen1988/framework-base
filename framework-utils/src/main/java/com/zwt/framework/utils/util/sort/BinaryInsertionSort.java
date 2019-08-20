package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 折半插入排序（BinaryInsertionSort）
 * 原理：
 * 插入排序算法的优化算法
 * 排序算法是和已排序队列按顺序一一比较，然后交换位置，直到找出插入位置。折半插入排序算法是先适用折半查找找出插入位置，然后统一后移。
 * 注意点：
 * 时间复杂度和排序完成度没有关系，和队列大小有关系。和插入排序比起来，因为使用了折半查找，所以理论上比较次数平均会少一些。
 * 折半查找只是减少了比较次数，但是元素的移动次数不变
 * 折半插入排序 平均时间复杂度为O(n^2)；空间复杂度为O(1)；是稳定的排序算法
 * @date 2019/8/14
 * @since 1.0
 */
public class BinaryInsertionSort {
    public static int[] binaryInsertionSort(int[] data) {
        for (int i = 1, len = data.length; i < len; i++) {
            // 要插入的元素
            int temp = data[i];
            int low = 0;
            int high = i - 1;
            // 折半比较，直到找到low大于high时（找到比他大的值的位置low）
            while(low <= high) {
                int mid = (low+high)/2;
                if (data[mid] > temp) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            // 移动 比他大的值，全部后移
            for (int j = i; j > low; j--) {
                data[j] = data[j-1];
            }
            // 插入
            data[low] = temp;
        }
        return data;
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("BinaryInsertionSort排序前：");
        System.out.println(Arrays.toString(a));
        int[] s = binaryInsertionSort(a);
        System.out.println("BinaryInsertionSort排序后：");
        System.out.println(Arrays.toString(s));
    }
}
