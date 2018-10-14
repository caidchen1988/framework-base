package com.zwt.framework.utils.util.other;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 取数组的前n个大元素
 * @date 2018/9/22
 * @since 1.0
 */
public class Test {
    /**
     * JDK方法
     * @param array
     * @return
     */
    private static int[] findTopNFromArray(int [] array,int n){
        Arrays.sort(array);
        int[] result=new int[n];
        System.arraycopy(array,array.length-n,result,0,n);
        return result;
    }
    /**

     * 求数组的前n个元素
     *
     * @param anyOldOrderValues
     * @param n
     * @return
     */
    public static int[] findTopNValues(int[] anyOldOrderValues, int n) {
        int[] result = new int[n];
        findTopNValues(anyOldOrderValues, 0, anyOldOrderValues.length - 1, n,
                n, result);
        return result;
    }

    public static final void findTopNValues(int[] a, int p, int r, int i,
                                            int n, int[] result) {
        // 全部取到，直接返回
        if (i == 0)
            return;
        // 只剩一个元素，拷贝到目标数组
        if (p == r) {
            System.arraycopy(a, p, result, n - i, i);
            return;
        }
        int len = r - p + 1;
        if (i > len || i < 0)
        throw new IllegalArgumentException();

        // 划分
        int q = medPartition(a, p, r);
        // 计算右子段长度
        int k = r - q + 1;
        // 右子段长度恰好等于i
        if (i == k) {
            // 拷贝右子段到结果数组，返回
            System.arraycopy(a, q, result, n - i, i);
            return;
        } else if (k > i) {
            // 右子段比i长，递归到右子段求前i个元素
            findTopNValues(a, q + 1, r, i, n, result);
        } else {
            // 右子段比i短，拷贝右子段到结果数组，递归左子段求前i-k个元素
            System.arraycopy(a, q, result, n - i, k);
            findTopNValues(a, p, q - 1, i - k, n, result);
        }
    }

    public static int medPartition(int x[], int p, int r) {
        int len = r - p + 1;
        int m = p + (len >>1);
        if (len > 7) {
            int l = p;
            int n = r;
            if (len > 40) { // Big arrays, pseudomedian of 9
                int s = len / 8;
                l = med3(x, l, l + s, l + 2 * s);
                m = med3(x, m - s, m, m + s);
                n = med3(x, n - 2 * s, n - s, n);
            }
            m = med3(x, l, m, n); // Mid-size, med of 3
        }
        if (m != r) {
            int temp = x[m];
            x[m] = x[r];
            x[r] = temp;
        }
        return partition(x, p, r);
    }

    private static int med3(int x[], int a, int b, int c) {
        return x[a] < x[b] ? (x[b] < x[c] ? b : x[a] < x[c] ? c : a)
            : x[b] > x[c] ? b : x[a] > x[c] ? c : a;
    }

    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static int partition(int a[], int p, int r) {
        int x = a[r];
        int m = p - 1;
        int j = r;
        while (true) {
            do {
                j--;
            } while (j>=p&&a[j] > x);
            do {
                m++;
            } while (a[m] < x);

            if (j < m)
            break;
            swap(a, m, j);
        }
        swap(a, r, j + 1);
        return j + 1;
    }


    /**
     * 数据测试
     * @param args
     */
    public static void main(String[] args) throws Exception{
        Thread.sleep(2000);
        int [] xArrays=new int [1000000];
        Random random=new Random();
        for(int i=0;i<xArrays.length;i++){
            xArrays[i]=random.nextInt();
        }

        long starttime1=System.nanoTime();
        findTopNValues(xArrays,100);
        long endtime1=System.nanoTime();
        System.out.println(endtime1-starttime1);

        long starttime2=System.nanoTime();
        findTopNFromArray(xArrays,100);
        long endtime2=System.nanoTime();
        System.out.println(endtime2-starttime2);

    }



}
