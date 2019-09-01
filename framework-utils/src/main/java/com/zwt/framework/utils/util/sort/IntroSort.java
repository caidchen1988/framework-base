package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 内省排序（Introspective Sort）
 * 该排序算法的主要策略：
 * 1.数据量大时采用QuickSort，分段递归排序。
 * 2.一旦分段后的数据量小于某个门槛，为避免Quick Sort的递归调用带来的额外负荷，就改用Insertion Sort。
 * 3.如果层次过深，还会改用HeapSort
 * 4.“三点中值”获取好的分割
 *
 * 内省排序是一个比较排序算法
 *
 * 该排序复杂度情况：
 * 最差时间复杂度 O(n*log n)
 * 平均时间复杂度 O(n*log n)
 * 最优时间复杂度 O(n*log n)
 * 空间复杂度     O(log n)
 * 是否稳定       否
 * @date 2019/8/13
 * @since 1.0
 */
public class IntroSort {
    /**
     * 数据量的分界线，决定了使用quick sort/heap sort还是insertion sort
     */
    private static final int THRESHOLD = 16;
    /**
     * 堆排序用到的辅助函数
     * @param i
     * @return
     */
    private static int parent(int i) {
        return (i - 1) / 2;
    }
    private static int left(int i) {
        return 2 * i + 1;
    }
    private static int right(int i) {
        return (2 * i + 2);
    }
    private static void swap(int[] array, int index1, int index2) {
        int temp;
        temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
    private static void heapShiftDown(int[] heap, int i, int begin, int end) {
        int l = left(i - begin) + begin;
        int r = right(i - begin) + begin;
        int largest = i;
        //找出左右字节点与父节点中的最大者
        if (l < end && heap[l] > heap[largest]) {
            largest = l;
        }
        if (r < end && heap[r] > heap[largest]) {
            largest = r;
        }
        //若最大者不为父节点，则需交换数据，并持续向下滚动至满足最大堆特性
        if (largest != i) {
            swap(heap, largest, i);
            heapShiftDown(heap, largest, begin, end);
        }
    }
    /**
     * 自底向上的开始建堆，即从堆的倒数第二层开始
     * @param heap
     * @param begin
     * @param end
     */
    private static void buildHeap(int[] heap, int begin, int end) {
        for (int i = (begin + end) / 2; i >= begin; i--) {
            heapShiftDown(heap, i, begin, end);
        }
    }
    /**
     * 堆排序
     * @param heap
     * @param begin
     * @param end
     */
    private static void heapSort(int[] heap, int begin, int end) {
        buildHeap(heap, begin, end);
        for (int i = end; i > begin; i--) {
            swap(heap, begin, i);
            heapShiftDown(heap, begin, begin, i);
        }
    }
    /**
     * 插入排序
     * @param array
     * @param len
     */
    private static void insertionSort(int[] array, int len) {
        int i, j, temp;
        for (i = 1; i < len; i++) {
            //store the original sorted array in temp
            temp = array[i];
            //compare the new array with temp(maybe -1?)
            for (j = i; j > 0 && temp < array[j - 1]; j--) {
                //all larger elements are moved one pot to the right
                array[j] = array[j - 1];
            }
            array[j] = temp;
        }
    }
    /**
     * 三点中值计算
     * @param array
     * @param first
     * @param median
     * @param end
     * @return
     */
    private static int median3(int[] array, int first, int median, int end) {
        if (array[first] < array[median]) {
            return helpMethod(array, first, median, end);
        } else {
            return helpMethod(array, median, first, end);
        }
    }
    private static int helpMethod(int[] array, int first, int median, int end) {
        if (array[median] < array[end]) {
            return median;
        } else if (array[first] < array[end]) {
            return end;
        } else {
            return first;
        }
    }
    /**
     * 对数组分割
     * @param array
     * @param left
     * @param right
     * @param p
     * @return
     */
    private static int partition(int[] array, int left, int right, int p) {
        //选择最右侧的元素作为分割标准
        int index = left;
        swap(array, p, right);
        int pivot = array[right];
        //将所有小于标准的点移动到index的左侧
        for (int i = left; i < right; i++) {
            if (array[i] < pivot) {
                swap(array, index++, i);
            }
        }
        //将标准与index指向的元素交换，返回index，即分割位置
        swap(array, right, index);
        return index;
    }
    /**
     * 递归的对数组进行分割排序
     * @param array
     * @param begin
     * @param end
     * @param depthLimit
     */
    private static void introSortLoop(int[] array, int begin, int end, int depthLimit) {
        //子数组数据量大小，则交给后面的插入排序进行处理
        while ((end - begin + 1) > THRESHOLD) {
            //递归深度过大，则由堆排序代替
            if (depthLimit == 0) {
                heapSort(array, begin, end);
                return;
            }
            --depthLimit;
            //使用quick sort进行排序
            int cut = partition(array, begin, end,
                    median3(array, begin, begin + (end - begin) / 2, end));
            introSortLoop(array, cut, end, depthLimit);
            //对左半段进行递归的sort
            end = cut;
        }
    }
    /**
     * 计算最大容忍的递归深度
     * @param n
     * @return
     */
    private static int lg(int n) {
        int k;
        for (k = 0; n > 1; n >>= 1) {
            ++k;
        }
        return k;
    }
    /**
     * IntroSort排序
     * @param array
     * @param len
     */
    public static void introSort(int[] array, int len) {
        if (len != 1) {
            introSortLoop(array, 0, len - 1, lg(len) * 2);
            insertionSort(array, len);
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("IntroSort排序前：");
        System.out.println(Arrays.toString(a));
        introSort(a,a.length);
        System.out.println("IntroSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
