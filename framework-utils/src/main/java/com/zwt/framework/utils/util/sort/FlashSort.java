package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 闪电排序（FlashSort）
 * <p>
 * FlashSort在O(n)时间内对n个元素进行排序。
 * Flash-Sort在第一步中使用长度为m的向量L(k)对数组a的元素进行分类，然后在第二步中累积结果计数，L(k)指向类边界。
 * 然后对元素进行原位排列。在置换过程中，L(k)在数组a中类k的一个元素的每次新位置上都被一个单位步长递减。
 * 一个周期结束,如果向量L (k)指向下一个元素的位置classboundary类k。
 * 新周期的领袖的元素位于最低位置服从互补条件,即L (k)指向的位置与L (k(A(i)))> =i。
 * 除了数组长度为n的拥有的n个元素进行排序,唯一辅助向量是L (k)向量。
 * 这个向量的大小等于与n相比较小的类的数量m，例如，在浮点数的情况下，m通常可以设置为m=0.1 n，或者在字节版本中，m=256。
 * 最后，通过递归或简单的传统排序算法，在类中局部地排序少量可部分区分的元素。
 * http://www.neubert.net/FSOIntro.html
 * <p>
 * 预测的思想
 * <p>
 * 如果有这样一个待排数组,其最大值是100,最小值是1,数组长度为100,那么50在排完序后极有可能出现在正中间,flash sort就是基于这个思路
 * <p>
 * 预测公式：
 * K(Ai) = 1 + INT((m - 1)(Ai - Amin)/(Amax - Amin))
 * @date 2019/8/15
 * @since 1.0
 */
public class FlashSort {
    public static void flashSort(int[] array) {
        //桶排序
        partialFlashSort(array, array.length);
        //桶内元素使用插入排序
        insertionSort(array);
    }
    private static void partialFlashSort(int[] a, int n) {
        //m值，取0.1n，也可以自由指定
        int bucketSize = n / 10;
        if (bucketSize < 1) {
            bucketSize = 1;
        }
        //构建bucket
        int[] buckets = new int[bucketSize];
        int i, j, k;
        int min = a[0];
        int maxIndex = 0;

        //找到最大最小值
        for (i = 1; i < n; i++) {
            if (a[i] < min) {
                min = a[i];
            }
            if (a[i] > a[maxIndex]) {
                maxIndex = i;
            }
        }
        if (min == a[maxIndex]) {
            return;
        }
        //计算系数
        double c1 = ((double) bucketSize - 1) / (a[maxIndex] - min);
        //计算元素位置
        for (i = 0; i < n; i++) {
            k = (int) (c1 * (a[i] - min));
            buckets[k]++;
        }
        for (k = 1; k < bucketSize; k++) {
            buckets[k] += buckets[k - 1];
        }
        //元素入桶
        int hold = a[maxIndex];
        a[maxIndex] = a[0];
        a[0] = hold;

        int nmove = 0;
        int flash;
        j = 0;
        k = bucketSize - 1;

        while (nmove < n - 1) {
            while (j > (buckets[k] - 1)) {
                j++;
                k = (int) (c1 * (a[j] - min));
            }
            flash = a[j];
            while (j != buckets[k]) {
                k = (int) (c1 * (flash - min));
                hold = a[buckets[k] - 1];
                a[buckets[k] - 1] = flash;
                flash = hold;
                buckets[k]--;
                nmove++;
            }
        }
    }
    private static void insertionSort(int[] a) {
        int i, j, hold;
        for (i = a.length - 3; i >= 0; i--) {
            if (a[i + 1] < a[i]) {
                hold = a[i];
                j = i;
                while (a[j + 1] < hold) {
                    a[j] = a[j + 1];
                    j++;
                }
                a[j] = hold;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("FlashSort排序前：");
        System.out.println(Arrays.toString(a));
        flashSort(a);
        System.out.println("FlashSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
