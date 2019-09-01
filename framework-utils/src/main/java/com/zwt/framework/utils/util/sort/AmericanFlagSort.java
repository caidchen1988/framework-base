package com.zwt.framework.utils.util.sort;

/**
 * @author zwt
 * @detail 美国旗帜排序
 * @date 2019/8/15
 * @since 1.0
 */

import java.util.Arrays;
import java.util.Random;

/**
 * An American flag sort is an efficient, in-place variant of radix sort that
 * distributes items into hundreds of buckets. Non-comparative sorting
 * algorithms such as radix sort and American flag sort are typically used to
 * sort large objects such as strings, for which comparison is not a unit-time
 * operation.
 * <p>
 * Family: Bucket.<br>
 * Space: In-place.<br>
 * Stable: False.<br>
 * <p>
 * Average case = O(n*k/d)<br>
 * Worst case = O(n*k/d)<br>
 * Best case = O(n*k/d)<br>
 * <p>
 * NOTE: n is the number of digits and k is the average bucket size
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/American_flag_sort">American Flag Sort (Wikipedia)</a>
 */
public class AmericanFlagSort {
    /**
     * 10位数的基数是10
     */
    private static final int NUMBER_OF_BUCKETS = 10;

    public static void americanFlagSort(int[] unsorted) {
        // Max number of digits
        int numberOfDigits = getMaxNumberOfDigits(unsorted);
        int max = 1;
        for (int i = 0; i < numberOfDigits - 1; i++) {
            max *= 10;
        }
        sort(unsorted, 0, unsorted.length, max);
    }
    private static void sort(int[] unsorted, int start, int length, int divisor) {
        // First pass - find counts
        int[] count = new int[NUMBER_OF_BUCKETS];
        int[] offset = new int[NUMBER_OF_BUCKETS];
        int digit = 0;
        for (int i = start; i < length; i++) {
            int d = unsorted[i];
            digit = getDigit(d, divisor);
            count[digit]++;
        }
        offset[0] = start;
        for (int i = 1; i < NUMBER_OF_BUCKETS; i++) {
            offset[i] = count[i - 1] + offset[i - 1];
        }
        // Second pass - move into position
        for (int b = 0; b < NUMBER_OF_BUCKETS; b++) {
            while (count[b] > 0) {
                int origin = offset[b];
                int from = origin;
                int num = unsorted[from];
                unsorted[from] = -1;
                do {
                    digit = getDigit(num, divisor);
                    int to = offset[digit]++;
                    count[digit]--;
                    int temp = unsorted[to];
                    unsorted[to] = num;
                    num = temp;
                    from = to;
                } while (from != origin);
            }
        }
        if (divisor > 1) {
            // Sort the buckets
            for (int i = 0; i < NUMBER_OF_BUCKETS; i++) {
                int begin = (i > 0) ? offset[i - 1] : start;
                int end = offset[i];
                if (end - begin > 1) {
                    sort(unsorted, begin, end, divisor / 10);
                }
            }
        }
    }
    /**
     * 获取最大值 位长度
     * @param unsorted
     * @return
     */
    private static int getMaxNumberOfDigits(int[] unsorted) {
        int max = Integer.MIN_VALUE;
        int temp = 0;
        for (int i : unsorted) {
            temp = (int) Math.log10(i) + 1;
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }
    /**
     * 获取该位数字
     * @param integer
     * @param divisor
     * @return
     */
    private static int getDigit(int integer, int divisor) {
        return (integer / divisor) % 10;
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("AmericanFlagSort排序前：");
        System.out.println(Arrays.toString(a));
        americanFlagSort(a);
        System.out.println("AmericanFlagSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
