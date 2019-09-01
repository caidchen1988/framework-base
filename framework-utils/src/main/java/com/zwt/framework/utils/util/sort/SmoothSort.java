package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail Smooth排序算法 （平滑排序算法）
 *     /**
 *      * 最好时间复杂度　　O(n)
 *      * 平均时间复杂度　　O(nlogn)
 *      * 最坏时间复杂度　　O(nlogn)
 *      * 空间复杂度　　　　O(1)
 *      * 是否稳定　　　　　否
 *      *     一种基于堆排序的排序算法，使用斐波那契数列，在要排序的项目已经部分排序的情况下，比堆排序表现得更好。
 *      * 　　Smooth Sort基本思想和Heap Sort相同，但Smooth Sort使用的是一种由多个堆组成的优先队列，这种优先队列在取出最大元素后剩余元素可以就地调整成优先队列，所以Smooth Sort不用像Heap Sort那样反向地构建堆，在数据基本有序时可以达到O(n)复杂度。
 *      * 　　Smooth Sort是所有算法中时间复杂度理论值最好的，但由于Smooth Sort所用的优先队列是基于一种不平衡的结构，复杂度因子很大，所以该算法的实际效率并不是很好。
 *      *
 * @date 2019/8/13
 * @since 1.0
 */
public class SmoothSort {
    /**
     * 交换指定数组两个数的位置
     * @param array
     * @param index1
     * @param index2
     */
    private static void swap(int[] array,int index1,int index2) {
        int temp =  array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
    /**
     * 斐波那契数列
     */
    private static int[] leonardo = new int[]{
            1, 1, 3, 5, 9, 15, 25, 41, 67, 109, 177, 287, 465, 753, 1219, 1973,
            3193, 5167, 8361, 13529, 21891, 35421, 57313, 92735, 150049, 242785,
            392835, 635621, 1028457, 1664079, 2692537, 4356617, 7049155, 11405773,
            18454929, 29860703, 48315633, 78176337, 126491971, 204668309, 331160281,
            535828591, 866988873, 1402817465
    };
    /**
     * 堆调整函数
     * @param array
     * @param currentHeap
     * @param levelIndex
     * @param levels
     */
    private static void smoothSortFix(int[] array, int currentHeap, int levelIndex, int[] levels) {
        int prevHeap;
        int maxChild;
        int childHeap1;
        int childHeap2;
        int currentLevel;

        while(levelIndex > 0) {
            prevHeap = currentHeap - leonardo[levels[levelIndex]];
            if(array[currentHeap]< array[prevHeap]) {
                if(levels[levelIndex] > 1) {
                    childHeap1 = currentHeap - 1 - leonardo[levels[levelIndex] - 2];
                    childHeap2 = currentHeap - 1;
                    if(array[prevHeap]< array[childHeap1]) {
                        break;
                    }
                    if(array[prevHeap] < array[childHeap2]){
                        break;
                    }
                }
                swap(array,currentHeap,prevHeap);
                currentHeap = prevHeap;
                levelIndex -= 1;
            } else {
                break;
            }
        }
        currentLevel = levels[levelIndex];
        while(currentLevel > 1) {
            maxChild = currentHeap;
            childHeap1 = currentHeap - 1 - leonardo[currentLevel - 2];
            childHeap2 = currentHeap - 1;

            if(array[maxChild]< array[childHeap1]){
                maxChild = childHeap1;
            }
            if(array[maxChild]< array[childHeap2]) {
                maxChild = childHeap2;
            }
            if(maxChild == childHeap1) {
                swap(array,currentHeap, childHeap1);
                currentHeap = childHeap1;
                currentLevel -= 1;
            }else if(maxChild == childHeap2) {
                swap(array,currentHeap, childHeap2);
                currentHeap = childHeap2;
                currentLevel -= 2;
            } else {
                break;
            }
        }
    }
    /**
     * Smooth排序算法
     * @param array
     * @param size
     */
    public static void smoothSort(int[] array, int size) {
        int[] levels = new int[32];
        int toplevel = 0;
        int i;
        for(i = 1; i < size; i++) {
            if(toplevel > 0 && levels[toplevel - 1] - levels[toplevel] == 1) {
                toplevel -= 1;
                levels[toplevel] += 1;
            } else if(levels[toplevel] != 1) {
                toplevel += 1;
                levels[toplevel] = 1;
            } else {
                toplevel += 1;
                levels[toplevel] = 0;
            }
            smoothSortFix(array, i, toplevel, levels);
        }
        for(i = size - 2; i > 0; i--) {
            if(levels[toplevel] <= 1) {
                toplevel -= 1;
            } else {
                levels[toplevel] -= 1;
                levels[toplevel + 1] = levels[toplevel] - 1;
                toplevel += 1;

                smoothSortFix(array, i - leonardo[levels[toplevel]], toplevel - 1, levels);
                smoothSortFix(array, i, toplevel, levels);
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[10];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("SmoothSort排序前：");
        System.out.println(Arrays.toString(a));
        smoothSort(a,a.length);
        System.out.println("SmoothSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
