package com.zwt.framework.utils.util.sort.help;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author zwt
 * @detail
 * @date 2019/8/19
 * @since 1.0
 */
public class IntroSortGIFMakeJFrame extends JFrame {
    public static void main(String[] args) {
        IntroSortGIFMakeJFrame introSortGIFMakeJFrame = new IntroSortGIFMakeJFrame();
        IntroSortGIFMakePanel introSortGIFMakePanel = new IntroSortGIFMakePanel();
        introSortGIFMakeJFrame.add(introSortGIFMakePanel);
        introSortGIFMakeJFrame.setSize(1900,1000);
        introSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        introSortGIFMakeJFrame.setVisible(true);
    }
    public IntroSortGIFMakeJFrame(){
    }
}

class IntroSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int[] a = new int[40];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(80)+1;
        }
        IntroSortGIFMakeHelper.introSort(g,a,a.length);
    }
}

class IntroSortGIFMakeHelper {
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

    private static void swap(Graphics g,int[] array, int index1, int index2,Color color) {
        drawPicture(g,array,index1,index2,color);
        int temp;
        temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
        drawPicture(g,array,index1,index2,color);
    }

    private static void heapShiftDown(Graphics g,int[] heap, int i, int begin, int end) {
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
            swap(g,heap, largest, i,Color.PINK);
            heapShiftDown(g,heap, largest, begin, end);
        }
    }

    /**
     * 自底向上的开始建堆，即从堆的倒数第二层开始
     * @param heap
     * @param begin
     * @param end
     */
    private static void buildHeap(Graphics g,int[] heap, int begin, int end) {
        for (int i = (begin + end) / 2; i >= begin; i--) {
            heapShiftDown(g,heap, i, begin, end);
        }
    }

    /**
     * 堆排序
     * @param heap
     * @param begin
     * @param end
     */
    private static void heapSort(Graphics g,int[] heap, int begin, int end) {
        buildHeap(g,heap, begin, end);
        for (int i = end; i > begin; i--) {
            swap(g,heap, begin, i,Color.PINK);
            heapShiftDown(g,heap, begin, begin, i);
        }
    }

    /**
     * 插入排序
     * @param array
     * @param len
     */
    private static void insertionSort(Graphics g,int[] array, int len) {
        int i, j, temp;
        for (i = 1; i < len; i++) {
            //store the original sorted array in temp
            temp = array[i];
            //compare the new array with temp(maybe -1?)
            for (j = i; j > 0 && temp < array[j - 1]; j--) {
                drawPicture(g,array,i,j,Color.RED);
                //all larger elements are moved one pot to the right
                array[j] = array[j - 1];
            }
            array[j] = temp;
            drawPicture(g,array,i,j,Color.RED);
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
    private static int partition(Graphics g,int[] array, int left, int right, int p) {
        //选择最右侧的元素作为分割标准
        int index = left;
        swap(g,array, p, right,Color.MAGENTA);
        int pivot = array[right];
        //将所有小于标准的点移动到index的左侧
        for (int i = left; i < right; i++) {
            if (array[i] < pivot) {
                swap(g,array, index++, i,Color.MAGENTA);
            }
        }
        //将标准与index指向的元素交换，返回index，即分割位置
        swap(g,array, right, index,Color.MAGENTA);
        return index;
    }

    /**
     * 递归的对数组进行分割排序
     * @param array
     * @param begin
     * @param end
     * @param depthLimit
     */
    private static void introSortLoop(Graphics g,int[] array, int begin, int end, int depthLimit) {
        //子数组数据量大小，则交给后面的插入排序进行处理
        while ((end - begin + 1) > THRESHOLD) {
            //递归深度过大，则由堆排序代替
            if (depthLimit == 0) {
                heapSort(g,array, begin, end);
                return;
            }
            --depthLimit;
            //使用quick sort进行排序
            int cut = partition(g,array, begin, end,
                    median3(array, begin, begin + (end - begin) / 2, end));
            introSortLoop(g,array, cut, end, depthLimit);
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
     * IntroSort排序入口
     * @param array
     * @param len
     */
    public static void introSort(Graphics g,int[] array, int len) {
        drawPicture(g,array,-1,-1,Color.BLUE);
        if (len != 1) {
            introSortLoop(g,array, 0, len - 1, lg(len) * 2);
            insertionSort(g,array, len);
        }
        drawPicture(g,array,-1,-1,Color.BLUE);
    }

    //图形单位宽度
    private static int width = 20;
    //图形单位高度
    private static int baseHeight = 10;

    /**
     * 画出当前数组状态，蓝色
     * @param g
     * @param array  数组
     * @param idx1   该值为数组索引，-1不会标记，其它会标记为红色，说明进度
     * @param idx2   该值为数组索引，-1不会标记，其它会标记为红色，说明进度
     */
    public static void drawPicture(Graphics g, int[] array, int idx1, int idx2,Color color){
        //水印，没什么用
        int waterX = 500;
        int waterY = 950;
        g.setColor(Color.gray);
        g.drawString("www.sakuratears.top",waterX,waterY);

        //绘制矩形
        int startX = 100;
        int startY = 900;
        for(int i=0;i<array.length;i++){
            if(i==idx1 || i==idx2){
                g.setColor(color);
                g.fillRect(startX,startY-baseHeight * array[i],width,baseHeight * array[i]);
                g.drawString(array[i]+"",startX+width/2 - 5,startY-baseHeight * array[i]-width/2);
                startX += width;
            }else{
                g.setColor(Color.BLUE);
                g.fillRect(startX,startY-baseHeight * array[i],width,baseHeight * array[i]);
                g.drawString(array[i]+"",startX+width/2 - 5,startY-baseHeight * array[i]-width/2);
                startX += width;
            }
        }
        //等待1s
        try{
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }
        //清空矩形区域重新绘图
        g.clearRect(0,0,1900,1000);
    }
}
