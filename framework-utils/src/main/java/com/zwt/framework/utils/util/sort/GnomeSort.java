package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 侏儒排序（GnomeSort）
 * 号称最简单（程序简单）的排序算法,只有一层循环,默认情况下前进冒泡,一旦遇到冒泡的情况发生就往回冒,直到把这个数字放好为止。
 * 可以通过引入变量来优化gnome排序，以在遍历到列表的开头之前存储位置。 通过此优化，侏儒排序将成为插入排序的变体。
 *
 * 该排序复杂度情况：
 * 最差时间复杂度 O(n^2)
 * 平均时间复杂度 O(n^2)
 * 最优时间复杂度 O(n)
 * 空间复杂度     O(l)
 * 是否稳定       是
 * @date 2019/8/13
 * @since 1.0
 */
public class GnomeSort {
    public static void gnomeSort(int[] ar) {
        int n = ar.length;
        int i = 0;
        while (i < n) {
            if (i == 0 || ar[i - 1] <= ar[i]) {
                i++;
            } else {
                int tmp = ar[i];
                ar[i] = ar[i - 1];
                ar[--i] = tmp;
            }
        }
    }
    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("GnomeSort排序前：");
        System.out.println(Arrays.toString(a));
        gnomeSort(a);
        System.out.println("GnomeSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
