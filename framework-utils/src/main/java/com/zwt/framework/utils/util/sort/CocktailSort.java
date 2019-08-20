package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 鸡尾酒排序 CocktailSort（冒泡排序变种）
 * 鸡尾酒排序又称双向冒泡排序, 是冒泡排序的一种变形。该算法与冒泡排序的不同处在于排序时是以双向在序列中进行排序。
 * 鸡尾酒排序等于是冒泡排序的轻微变形。不同的地方在于从低到高然后从高到低，而冒泡排序则仅从低到高去比较序列里的每个元素。他可以得到比冒泡排序稍微好一点的效能，原因是冒泡排序只从一个方向进行比对(由低到高)，每次循环只移动一个项目。
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
public class CocktailSort {
    public static void cocktailSort(int[] src) {
        for (int i = 0; i < src.length / 2; i++) {
            //将最小值排到队首
            for (int j = i; j < src.length - i - 1; j++) {
                if (src[j] > src[j + 1]) {
                    int temp = src[j];
                    src[j] = src[j + 1];
                    src[j + 1] = temp;
                }
            }
            //将最大值排到队尾
            for (int j = src.length - 1 - (i + 1); j > i; j--) {
                if (src[j] < src[j - 1]) {
                    int temp = src[j];
                    src[j] = src[j - 1];
                    src[j - 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("CocktailSort排序前：");
        System.out.println(Arrays.toString(a));
        cocktailSort(a);
        System.out.println("CocktailSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
