package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 圈排序(CycleSort)
 *
 * 给定一个对象序列，例如一个整数数组;如果这些元素没有按顺序排列，那是因为其中一些元素在它们之间交换了位置。
 * 例如，[4,1,2,3,5,0]不是按顺序排列的，因为4,5,0交换了位置(1个坏组)。
 * [4、2、3、7、5、0、1]顺序不对，因为4、5、0交换了位置，2、3、7、1交换了位置(两个坏组)。
 * 在离散数学中，每一组，无论好坏，都被称为一个周期或一个轨道。
 * cyclesort的基础是，如果每个坏组的元素返回到它们的正确位置，那么整个序列将被排序。
 * cyclesort有很大的优点，因为它提高了计算机内存的预期寿命。
 * 在cyclesort期间，每个元素最多移动一次。
 * 回想一下，每次更改内存块中的数据时，内存都会退化。
 *
 * 时间复杂度 平均 O(n^2)
 * 时间复杂度 最差 O(n^2)
 * 空间复杂度 O(1)
 * 是否稳定排序  否
 * @date 2019/8/14
 * @since 1.0
 */
public class CycleSort {
    public static void cycleSort(int[] array) {
        for (int cs = 0, seeker, pos; cs < array.length - 1; cs++) {
            //假设array[cs]中的元素不合适
            seeker = array[cs];
            pos = cs;
            //找到seeker的正确位置(pos)
            for (int i = cs + 1; i < array.length; i++) {
                if (array[i] < seeker) {
                    pos++;
                }
            }
            //如果seeker已经在正确的位置，继续
            if (pos == cs) {
                continue;
            }
            //复制后移动索引pos(如果有的话)
            while (seeker == array[pos]) {
                pos++;
            }
            //seeker放到了它正确的位置（索引pos处），同时原来pos处的元素成为了新的seeker，需要找到另一个位置
            seeker = set(array, seeker, pos);

            //在进入下一个循环之前完成当前循环。在当前周期结束时，pos==cs，因为一个周期总是在它开始的地方结束。
            while (pos != cs) {
                //代码同上
                pos = cs;
                for (int i = cs + 1; i < array.length; i++) {
                    if (array[i] < seeker) {
                        pos++;
                    }
                }
                while (seeker == array[pos]) {
                    pos++;
                }
                seeker = set(array, seeker, pos);
            }
        }
    }

    private static int set(int[] array, int data, int ndx) {
        try {
            return array[ndx];
        } finally {
            array[ndx] = data;
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("CycleSort排序前：");
        System.out.println(Arrays.toString(a));
        cycleSort(a);
        System.out.println("CycleSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
