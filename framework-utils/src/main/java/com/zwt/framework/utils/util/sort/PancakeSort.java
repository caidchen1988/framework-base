package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 煎饼排序 （PancakeSort）
 * 原理：
 * 我们有一个锅铲和一堆煎饼，我们的目标是将煎饼按照大小排序，大的在下面。
 * 我们唯一的办法是让锅铲从一个地方伸进去，并且把上面所有的煎饼翻下来。
 *
 * 时间复杂度 最差 O(n^2)
 * 时间复杂度 最好 O(n)
 * 空间复杂度 O(n)
 *
 * @date 2019/8/14
 * @since 1.0
 */
public class PancakeSort {
    /**
     * 翻转
     * @param n
     * @param heap
     */
    private static void flip(int n, int[] heap) {
        for (int i = 0; i < (n + 1) / 2; ++i) {
            int tmp = heap[i];
            heap[i] = heap[n - i];
            heap[n - i] = tmp;
        }
    }
    /**
     * 获取最小最大值数组
     * @param n
     * @param heap
     * @return
     */
    private static int[] minmax(int n, int[] heap) {
        int xm, xM;
        xm = xM = heap[0];
        int posm = 0, posM = 0;
        for (int i = 1; i < n; ++i) {
            if (heap[i] < xm) {
                xm = heap[i];
                posm = i;
            } else if (heap[i] > xM) {
                xM = heap[i];
                posM = i;
            }
        }
        return new int[]{posm, posM};
    }
    private static void sort(int n, int dir, int[] heap) {
        if (n == 0) {
            return;
        }
        int[] mM = minmax(n, heap);
        int bestXPos = mM[dir];
        int altXPos = mM[1 - dir];
        boolean flipped = false;

        if (bestXPos == n - 1) {
            --n;
        } else if (bestXPos == 0) {
            flip(n - 1, heap);
            --n;
        } else if (altXPos == n - 1) {
            dir = 1 - dir;
            --n;
            flipped = true;
        } else {
            flip(bestXPos, heap);
        }
        sort(n, dir, heap);
        if (flipped) {
            flip(n, heap);
        }
    }
    public static void pancakeSort(int[] array) {
        sort(array.length, 1, array);
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("PancakeSort排序前：");
        System.out.println(Arrays.toString(a));
        pancakeSort(a);
        System.out.println("PancakeSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
