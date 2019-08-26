package com.zwt.framework.utils.util.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author zwt
 * @detail 二叉排序树排序(BinaryTreeSort)
 *  原理：利用二叉树特性，较小值依次比较放左边，较大值依次比较放右边
 *
 *  时间复杂度 最好 O(n)
 *  时间复杂度 平均 O(n*log n)
 *  时间复杂度 最差 O(n*log n)
 *  空间复杂度 O(n)
 *  是否稳定  是
 * @date 2019/8/13
 * @since 1.0
 */
public class BinaryTreeSort {
    public static int[] binaryTreeSort(int[] array){
        BinaryNode root = new BinaryNode(array[0], null, null);
        for(int i=1; i<array.length; i++){
            root.addChild(array[i]);
        }
        List<Integer> list = BinaryNode.getSortedList(root);
        int[] result = new int[list.size()];
        for (int i=0;i<list.size();i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(10000);
        }
        System.out.println("BinaryTreeSort排序前：");
        System.out.println(Arrays.toString(a));
        int [] s = binaryTreeSort(a);
        System.out.println("BinaryTreeSort排序后：");
        System.out.println(Arrays.toString(s));
    }
}

/**
 * 二叉树节点类
 */
class BinaryNode{
    /**
     * 节点当前值
     */
    private int value;
    /**
     * 左节点
     */
    private BinaryNode lChild;
    /**
     * 右节点
     */
    private BinaryNode rChild;
    /**
     * 排序结果集
     */
    private static List<Integer> resultList = new ArrayList<>();

    public BinaryNode(int value, BinaryNode l, BinaryNode r){
        this.value = value;
        this.lChild = l;
        this.rChild = r;
    }
    public BinaryNode getLChild() {
        return lChild;
    }
    public BinaryNode getRChild() {
        return rChild;
    }
    public int getValue() {
        return value;
    }

    public static List<Integer> getSortedList(BinaryNode root) {
        iterate(root);
        return resultList;
    }
    /**
     * 添加一个节点
     * **/
    public void addChild(int n){
        if(n < value){
            if(lChild!=null){
                lChild.addChild(n);
            }else{
                lChild = new BinaryNode(n, null, null);
            }
        }else{
            if(rChild!=null){
                rChild.addChild(n);
            }else{
                rChild = new BinaryNode(n, null, null);
            }
        }
    }
    /**
     * 迭代赋值
     * @param root
     */
    public static void iterate(BinaryNode root){
        if(root.lChild!=null){
            iterate(root.getLChild());
        }
        resultList.add(root.getValue());
        if(root.rChild!=null){
            iterate(root.getRChild());
        }
    }
}
