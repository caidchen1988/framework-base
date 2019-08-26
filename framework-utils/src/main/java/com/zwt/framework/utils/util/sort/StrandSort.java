package com.zwt.framework.utils.util.sort;

import java.util.*;

/**
 * @author zwt
 * @detail StrandSort
 *
 * 时间复杂度 最好 O(n)
 * 时间复杂度 平均 O(n^2)
 * 时间复杂度 最差 O(n^2)
 * 空间复杂度 O(n)
 * 是否稳定  是
 * @date 2019/8/14
 * @since 1.0
 */
public class StrandSort {
    private static <E extends Comparable<? super E>> LinkedList<E> strandSort(LinkedList<E> list) {
        if (list.size() <= 1) {
            return list;
        }
        LinkedList<E> result = new LinkedList<>();
        while (list.size() > 0) {
            LinkedList<E> sorted = new LinkedList<>();
            //same as remove() or remove(0)
            sorted.add(list.removeFirst());
            for (Iterator<E> it = list.iterator(); it.hasNext(); ) {
                E elem = it.next();
                if (sorted.peekLast().compareTo(elem) <= 0) {
                    //same as add(elem) or add(0, elem)
                    sorted.addLast(elem);
                    it.remove();
                }
            }
            result = merge(sorted, result);
        }
        return result;
    }

    private static <E extends Comparable<? super E>> LinkedList<E> merge(LinkedList<E> left, LinkedList<E> right) {
        LinkedList<E> result = new LinkedList<>();
        while (!left.isEmpty() && !right.isEmpty()) {
            //change the direction of this comparison to change the direction of the sort
            if (left.peek().compareTo(right.peek()) <= 0) {
                result.add(left.remove());
            } else {
                result.add(right.remove());
            }
        }
        result.addAll(left);
        result.addAll(right);
        return result;
    }

    public static void strandSort(int[] array){
        LinkedList<Integer> linkedList = new LinkedList<>();
        Arrays.stream(array).forEach(e -> linkedList.add(e));
        List<Integer> list = strandSort(linkedList);
        assert list.size() == array.length;
        for(int i=0;i<array.length;i++){
            array[i] = list.get(i);
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("StrandSort排序前：");
        System.out.println(Arrays.toString(a));
        strandSort(a);
        System.out.println("StrandSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
