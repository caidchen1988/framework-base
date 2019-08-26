package com.zwt.framework.utils.util.sort;

import java.util.*;

/**
 * @author zwt
 * @detail 耐心排序 Patience Sort
 * 耐心排序（Patience Sort）是将数组的元素分类成很多堆再串接回数组的一种排序算法。
 * 受到纸牌游戏耐心的启发和命名。算法的变体有效地计算给定阵列中最长的增加子序列的长度。
 * <p>
 * 1.创建一个堆数组
 * 2.比较当前指向的元素和每个堆的第一个元素，计算出比当前元素小的堆数量
 * 3.若当前元素比所有堆的第一个元素大，创建新的堆并加入到堆数组中，否则将当前元素加入到第“比当前元素小的堆数量”个堆
 * 4.分类完后将每个堆反序然后对每个堆再做耐心排序
 * 5.最后将每个堆串接并存储回原本的数组
 *
 *
 *  该排序复杂度情况：
 *  最差时间复杂度 O(n*log n)
 *  平均时间复杂度 O(n*log n)
 *  最优时间复杂度 O(n)
 *  空间复杂度     O(n)
 *  是否稳定       否
 * @date 2019/8/13
 * @since 1.0
 */
public class PatienceSort {
    public static void patienceSort(int[] theArray) {
        List<List<Integer>> newList = new ArrayList<>();
        for (int i = 0; i < theArray.length; i++) {
            List<Integer> bucketList = new ArrayList<>();
            //先开始创建一个堆
            if (i == 0) {
                bucketList.add(theArray[i]);
                newList.add(bucketList);
            } else {
                boolean isOk = false;
                for (int j = 0; j < newList.size(); j++) {
                    //如果当前元素比堆内的第一个元素小，就放入该堆头部作为新的第一个元素，然后执行下个元素判断
                    if (theArray[i] < (int) ((List) newList.get(j)).get(0)) {
                        (newList.get(j)).add(0, theArray[i]);
                        isOk = true;
                        break;
                    }
                }
                //如果当前元素比所有堆内的第一个元素大，就创建个新堆，把元素作为第一个元素放进去
                if (!isOk) {
                    bucketList.add(theArray[i]);
                    newList.add(bucketList);
                }
            }
        }
        ////生成的堆合并，而后使用插入排序
        int q = 0;
        for (int m = 0; m < newList.size(); m++) {
            for (int n = 0; n < (newList.get(m)).size(); n++) {
                theArray[q] = (int) ((List) newList.get(m)).get(n);
                q++;
            }
        }
        //插入排序
        int tmp;
        int j;
        for (int i = 1; i < theArray.length; i++) {
            tmp = theArray[i];
            for (j = i - 1; j >= 0 && theArray[j] > tmp; j--) {
                theArray[j + 1] = theArray[j];
            }
            theArray[j + 1] = tmp;
        }
    }

    public static void main(String[] args) {
        int[] a = new int[200];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("PatienceSort排序前：");
        System.out.println(Arrays.toString(a));
        patienceSort(a);
        System.out.println("PatienceSort排序后：");
        System.out.println(Arrays.toString(a));
    }

    public static <E extends Comparable<? super E>> void patienceSort(E[] n) {
        List<Pile<E>> piles = new ArrayList<Pile<E>>();
        //生成堆
        for (E x : n) {
            Pile<E> newPile = new Pile<E>();
            newPile.push(x);
            int i = Collections.binarySearch(piles, newPile);
            if (i < 0){
                i = ~i;
            }
            if (i != piles.size()) {
                piles.get(i).push(x);
            }else {
                piles.add(newPile);
            }
        }
        //使用优先级队列处理数据
        PriorityQueue<Pile<E>> heap = new PriorityQueue<Pile<E>>(piles);
        for (int c = 0; c < n.length; c++) {
            Pile<E> smallPile = heap.poll();
            n[c] = smallPile.pop();
            if (!smallPile.isEmpty()) {
                heap.offer(smallPile);
            }
        }
        assert(heap.isEmpty());
    }
    private static class Pile<E extends Comparable<? super E>> extends Stack<E> implements Comparable<Pile<E>> {
        @Override
        public int compareTo(Pile<E> y) { return peek().compareTo(y.peek()); }
    }
}
