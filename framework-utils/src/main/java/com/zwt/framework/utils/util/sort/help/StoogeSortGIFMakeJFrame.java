package com.zwt.framework.utils.util.sort.help;

import javax.swing.*;
import java.awt.*;

/**
 * @author zwt
 * @detail
 * @date 2019/8/19
 * @since 1.0
 */
public class StoogeSortGIFMakeJFrame extends JFrame {
    public static void main(String[] args) {
        StoogeSortGIFMakeJFrame stoogeSortGIFMakeJFrame = new StoogeSortGIFMakeJFrame();
        StoogeSortGIFMakePanel stoogeSortGIFMakePanel = new StoogeSortGIFMakePanel();
        stoogeSortGIFMakeJFrame.add(stoogeSortGIFMakePanel);
        stoogeSortGIFMakeJFrame.setSize(700,800);
        stoogeSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stoogeSortGIFMakeJFrame.setVisible(true);
    }
    public StoogeSortGIFMakeJFrame(){
    }
}

class StoogeSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int [] array = new int[]{5,2,1,3,6,7,2,9,10,8,4};
        StoogeSortGIFMakeHelper.stoogeSort(g,array);
    }
}

class StoogeSortGIFMakeHelper {
    public static void stoogeSort(Graphics g,int[] array) {
        drawPicture(g,array,-1,-1);
        stoogeSort(g,array, 0, array.length - 1);
        drawPicture(g,array,-1,-1);
    }
    private static void stoogeSort(Graphics g,int[] array, int low, int high) {
        //如果第一个数大于最后一个数，交换位置
        if (array[low] > array[high]) {
            drawPicture(g,array,low,high);
            swap(array, low, high);
            drawPicture(g,array,low,high);
        }
        if (low + 1 >= high){
            return;
        }
        int third = (high - low + 1) / 3;
        //排序前2/3数组元素
        stoogeSort(g,array, low, high - third);
        //排序后2/3数组元素
        stoogeSort(g,array, low + third, high);
        //排序前2/3数组元素
        stoogeSort(g,array, low, high - third);
    }
    private static void swap(int[] a, int b, int c) {
        if (b == c){
            return;
        }
        int temp = a[b];
        a[b] = a[c];
        a[c] = temp;
    }

    //图形单位宽度
    private static int width = 50;
    //图形单位高度
    private static int baseHeight = 50;

    /**
     * 画出当前数组状态，蓝色
     * @param g
     * @param array  数组
     * @param idx1   该值为数组索引，-1不会标记，其它会标记为红色，说明进度
     * @param idx2   该值为数组索引，-1不会标记，其它会标记为红色，说明进度
     */
    public static void drawPicture(Graphics g, int[] array, int idx1, int idx2){
        //水印，没什么用
        int waterX = 400;
        int waterY = 700;
        g.setColor(Color.gray);
        g.drawString("www.sakuratears.top",waterX,waterY);

        //绘制矩形
        int startX = 100;
        int startY = 650;
        for(int i=0;i<array.length;i++){
            if(i==idx1 || i==idx2){
                g.setColor(Color.RED);
                g.fillRect(startX,startY-baseHeight * array[i],width,baseHeight * array[i]);
                g.drawString(array[i]+"",startX+width/2,startY-baseHeight * array[i]-width/2);
                startX += width;
            }else{
                g.setColor(Color.BLUE);
                g.fillRect(startX,startY-baseHeight * array[i],width,baseHeight * array[i]);
                g.drawString(array[i]+"",startX+width/2,startY-baseHeight * array[i]-width/2);
                startX += width;
            }
        }
        //等待1s
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        //清空矩形区域重新绘图
        g.clearRect(0,0,700,800);
    }
}
