package com.zwt.framework.utils.util.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zwt
 * @detail 锦标赛排序 （Tournament Sort） 别名 树形选择排序（Tree Selection Sort）
 *
 * 锦标赛排序
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(n)
 * 1.首先以n个元素为叶子节点建立一颗完全二叉树;(有技巧:叶子节点数量确定的完全
 * 二叉树是并不一定是唯一确定的，但是如果给定叶子节点数量必须比非叶子节点数量
 * 多1个而且只多1个，那么这棵二叉树就是完全确定的);
 * 1.1将当前叶子节点两两比较，将其父节点设置为较大的孩子节点，以此类推;
 * 1.2得到的这棵树中，树根节点就是所有n个节点中的最大值;
 * 1.3将这个树根节点对应的叶子节点的值设置为最小值，沿着该节点往树根方向逐个比较；
 * 并将节点之较大的拷贝给父节点;
 * 1.4最后得到的树根节点即为当前叶子节点中的最大节点;
 * ...
 * 直到最后只剩下一个其值未被修改的叶子节点；删除节点的顺序就是已排好序的一列元素;
 * 算法分析：
 * 第1次建立二叉树时需要开辟大小为2*n-1的节点空间，并执行n-1次比较;
 * 第2次执行了log(2n-1)次比较;
 * 第3次执行了log(2n-1)次比较;
 * ...
 * 第n次执行了log(2n-1)次比较;
 * 所以时间复杂度为O(2*n-1+(n-1)*log(2*n-1)) = O(n*log(n));
 *
 * @date 2019/8/15
 * @since 1.0
 */
public class TreeSelectionSort {
    public static void treeSelectionSort(int[] array) {
        // 数组长度
        int len = array.length;
        // 对一个满二叉树，节点总数 = 叶子节点数*2-1
        int nodeSize = len * 2 - 1;
        // 这里将用数组表示二叉树的存储结构
        int[] tree = new int[nodeSize + 1];
        /* 填充叶子节点 */
        for (int i = len - 1, j = 0; i >= 0; i--, j++) {
            tree[nodeSize - j] = array[i];
        }
        /* 填充其他节点 */
        for (int i = nodeSize - len; i > 0; i--) {
            tree[i] = tree[i * 2] < tree[i * 2 + 1] ? tree[i * 2] : tree[i * 2 + 1];
        }
        /* 将每次找出的最小元素移走 */
        // 数组a的索引
        int index = 0;
        // 最小值的索引
        int minIndex = 0;
        while (index < len) {
            // 这是tree的根节点，也是最小元素
            int min = tree[1];
            // 将tree中最小的元素取到a[0]中
            array[index++] = tree[1];
            minIndex = nodeSize;
            /* 从最后的叶子节点开始，直到找到最小值的索引 */
            while (tree[minIndex] != min) {
                minIndex--;
            }
            // 将这个最小元素置为最大
            tree[minIndex] = Integer.MAX_VALUE;
            /* 如果这个节点还有父节点，那么就将它的兄弟节点升到父亲节点位置 */
            // 根结点的索引是1
            while (minIndex > 1) {
                // 这个节点是左节点
                if (minIndex % 2 == 0) {
                    tree[minIndex / 2] = tree[minIndex] < tree[minIndex + 1] ? tree[minIndex] : tree[minIndex + 1];
                    minIndex = minIndex / 2;
                } else {// 这个节点是右节点
                    tree[minIndex / 2] = tree[minIndex] < tree[minIndex - 1] ? tree[minIndex] : tree[minIndex - 1];
                    minIndex = minIndex / 2;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[3];
        Random r = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(10000);
        }
        System.out.println("TreeSelectionSort排序前：");
        System.out.println(Arrays.toString(a));
        treeSelectionSort(a);
        System.out.println("TreeSelectionSort排序后：");
        System.out.println(Arrays.toString(a));
    }
}
