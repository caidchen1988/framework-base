package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 梳排序（CombSort）
 *
 * 梳排序是改良自冒泡排序和快速排序，其要旨在于消除乌龟（亦即在数组尾部的小数值），这些数值是造成冒泡排序缓慢的主因。
 * 相对地，兔子（亦即在数组前端的大数值），不影响冒泡排序的性能。
 *
 * 在冒泡排序中，只比较数组中相邻的二项，即比较的二项的间距（Gap）是1，梳排序提出此间距其实可大于1。
 * 梳排序中，开始时的间距设置为数组长度，并在循环中以固定比率递减，通常递减率设置为1.3。在一次循环中，梳排序如同冒泡排序一样把数组从首到尾扫描一次，比较及交换两项，不同的是两项的间距不固定于1。
 * 如果间距递减至1，梳排序假定输入数组大致排序好，并以冒泡排序作最后检查及修正。
 *递减率的设置影响着梳排序的效率，原作者以随机数作实验，得到最有效递减率为1.3的。
 * 如果此比率太小，则导致一循环中有过多的比较，如果比率太大，则未能有效消除数组中的乌龟。
 * 编程语言中乘法比除法快，故会取递减率倒数与间距相乘 0.8
 *
 * 梳排序的最差时间复杂度为 O(n^2)
 * 空间复杂度为O(1)
 * 是一种不稳定的排序算法
 *
 * @date 2019/8/13
 * @since 1.0
 */
public class CombSort {
    public static void combSort(int[] data) {
        int n = data.length;
        final double shrink = 1.3;
        int i, delta = n, noswap = 0;
        while (noswap == 0) {
            for (noswap = 1, i = 0; i + delta < n; i++) {
                if (data[i] > data[i + delta]) {
                    data[i] ^= data[i + delta];
                    data[i + delta] ^= data[i];
                    data[i] ^= data[i + delta];
                    noswap = 0;
                }
            }
            if (delta > 1) {
                delta /= shrink;
                noswap = 0;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("CombSort排序前：");
        System.out.println(Arrays.toString(a));
        combSort(a);
        System.out.println("CombSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
