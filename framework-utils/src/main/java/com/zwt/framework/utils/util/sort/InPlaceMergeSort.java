package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 原地归并排序（InPlaceMergeSort）
 * <p>
 * 原地归并排序特点：
 * <p>
 * 空间：不需要辅助数组即可归并，空间复杂度为O(1)
 * <p>
 * 时间：最差时间复杂度为O(n*(log n)^2)
 * <p>
 * https://www.cnblogs.com/xiaorenwu702/p/5880841.html
 * @date 2019/8/14
 * @since 1.0
 */
public class InPlaceMergeSort {
    public static void inPlaceMergeSort(int[] array) {
        inPlaceMergeSort(array, 0, array.length - 1);
    }
    private static void inPlaceMergeSort(int[] array, int first, int last) {
        int mid, lt, rt;
        int tmp;
        if (first >= last) {
            return;
        }
        mid = (first + last) / 2;
        inPlaceMergeSort(array, first, mid);
        inPlaceMergeSort(array, mid + 1, last);
        lt = first;
        rt = mid + 1;
        // One extra check:  can we SKIP the merge?
        if (array[mid] <= array[rt]) {
            return;
        }
        while (lt <= mid && rt <= last) {
            // Select from left:  no change, just advance lt
            if (array[lt] <= array[rt]) {
                lt++;
                // Select from right:  rotate [lt..rt] and correct
            } else {
                // Will move to [lt]
                tmp = array[rt];
                System.arraycopy(array, lt, array, lt + 1, rt - lt);
                array[lt] = tmp;
                // EVERYTHING has moved up by one
                lt++;
                mid++;
                rt++;
            }
        }
        // Whatever remains in [rt..last] is in place
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("InPlaceMergeSort排序前：");
        System.out.println(Arrays.toString(a));
        inPlaceMergeSort(a);
        System.out.println("InPlaceMergeSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
