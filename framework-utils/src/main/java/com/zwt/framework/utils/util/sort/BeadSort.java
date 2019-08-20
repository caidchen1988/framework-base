package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail  BeadSort 珠排序 又称重力排序
 * 时间复杂度   >= O(n)
 * 空间复杂度   >= O(n^2)
 * 稳定性    稳定
 * 实用性   不实用
 * @date 2019/8/13
 * @since 1.0
 */
public class BeadSort {
    public static void beadSort(int[] array) {
        int length = array.length;
        //获取待排序数组的最大值
        int max = array[0];
        int min = array[0];
        for (int i = 1; i < length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
            if(array[i] < min){
                min = array[i];
            }
        }
        //初始化算盘
        //构建一个二维数组，算盘最小长度应该为 max,高为 length
        int[][] bead = new int[max][length];
        for (int y=0;y<length;y++){
            for(int x=0;x<array[y];x++){
                bead[x][y] = 1;
            }
        }

        //珠子下落
        for(int x=0;x<max;x++){
            int count = 0;
            for(int y=0;y<length;y++){
                if(bead[x][y]==0){
                    count ++;
                }
            }
            //有值，需要移动
            if(count!=0){
                int[] temp = new int[max];
                for(int k=0;k<count;k++){
                    temp[k] = 1;
                }
                bead[x] = temp;
            }
        }

        //完成后取出数值
        for(int y=0;y<length;y++){
            int len = 0;
            for(int x=0;x<max;x++){
                if(bead[x][y]!=0){
                    len ++;
                }
            }
            array[y] = len;
        }

    }
    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("BeadSort排序前：");
        System.out.println(Arrays.toString(a));
        beadSort(a);
        System.out.println("BeadSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
