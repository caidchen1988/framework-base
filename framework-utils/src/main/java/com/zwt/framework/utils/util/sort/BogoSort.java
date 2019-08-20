package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail Bogo排序
 * 本排序的主要思想：
 * 运气
 * 基于原理：无限猴子定理（无限只猴子，在无限的时间内，随机敲击键盘，总有一只可以敲出莎士比亚全集）
 * 1.检查序列是否已排序，如果已排序，输出。
 * 2.随机打乱，跳回第一步。
 *
 * 时间复杂度 最好 O(n)
 * 时间复杂度 平均 O(n*n!)
 * 时间复杂度 最差 O(n*n! -> ∞ )
 * 空间复杂度 O(1)
 * 是否稳定   否
 *
 *  非常不实用的一种排序算法
 * @date 2019/8/14
 * @since 1.0
 */
public class BogoSort {
    public static void bogoSort(int[] array) {
        int size = array.length;
        int i, j;
        boolean tag;
        while (true) {
            tag = true;
            //检测是否有序
            for (i = 1; i < size; i++) {
                if (array[i] < array[i - 1]) {
                    tag = false;
                    break;
                }
            }
            //如果有序，则排序完成
            if (tag) {
                break;
            }
            //顺序不对，则随机打乱
            Random random = new Random();
            for (i = 0; i < size; i++) {
                j = random.nextInt(size);
                //随机交换两值
                swap(array, i, j);
            }
        }
    }
    private static void swap(int[] a, int p, int q) {
        int temp = a[p];
        a[p] = a[q];
        a[q] = temp;
    }
    public static void main(String[] args) {
        int[] a = new int[10];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("BogoSort排序前：");
        System.out.println(Arrays.toString(a));
        bogoSort(a);
        System.out.println("BogoSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
