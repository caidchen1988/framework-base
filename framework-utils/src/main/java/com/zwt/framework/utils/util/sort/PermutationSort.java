package com.zwt.framework.utils.util.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author zwt
 * @detail 全排序
 * 从n个不同元素中任取m（m≤n）个元素，按照一定的顺序排列起来，叫做从n个不同元素中取出m个元素的一个排列。当m=n时所有的排列情况叫全排列。
 *
 *时间复杂度 O(n!)
 * 空间复杂度 O(1)
 * 是否稳定 否
 *
 * 不实用的排序方法
 * @date 2019/8/14
 * @since 1.0
 */
public class PermutationSort {
    public static int[] permutationSort(int[] a) {
        List<int[]> list = new ArrayList<>();
        permute(a, a.length, list);
        for (int[] x : list) {
            if (isSorted(x)) {
                return x;
            }
        }
        return a;
    }
    //获取数组的全排列
    private static void permute(int[] a, int n, List<int[]> list) {
        if (n == 1) {
            int[] b = new int[a.length];
            System.arraycopy(a, 0, b, 0, a.length);
            list.add(b);
            return;
        }
        for (int i = 0; i < n; i++) {
            swap(a, i, n - 1);
            permute(a, n - 1, list);
            swap(a, i, n - 1);
        }
    }
    //判断数组是否有序
    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }
    //交换数组两数数值
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] a = new int[10];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("PermutationSort排序前：");
        System.out.println(Arrays.toString(a));
        int [] s = permutationSort(a);
        System.out.println("PermutationSort排序后：");
        System.out.println(Arrays.toString(s));
    }
}
