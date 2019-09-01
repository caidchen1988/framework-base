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
public class SmoothSortGIFMakeJFrame extends JFrame {
    public static void main(String[] args) {
        SmoothSortGIFMakeJFrame smoothSortGIFMakeJFrame = new SmoothSortGIFMakeJFrame();
        SmoothSortGIFMakePanel smoothSortGIFMakePanel = new SmoothSortGIFMakePanel();
        smoothSortGIFMakeJFrame.add(smoothSortGIFMakePanel);
        smoothSortGIFMakeJFrame.setSize(1900,1000);
        smoothSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        smoothSortGIFMakeJFrame.setVisible(true);
    }
    public SmoothSortGIFMakeJFrame(){
    }
}

class SmoothSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int[] a = new int[16];
        Random r = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = r.nextInt(80)+1;
        }
        SmoothSortGIFMakeHelper.smoothSort(g,a,a.length);
    }
}

class SmoothSortGIFMakeHelper {
    private static void swap(Graphics g,int[] array,int index1,int index2) {
        drawPicture(g,array,index1,index2,Color.RED);
        int temp =  array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
        drawPicture(g,array,index1,index2,Color.RED);
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
     * 排序修正函数
     * @param array
     * @param currentHeap
     * @param levelIndex
     * @param levels
     */
    private static void smoothSortFix(Graphics g,int[] array, int currentHeap, int levelIndex, int[] levels) {
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
                swap(g,array,currentHeap,prevHeap);
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
                swap(g,array,currentHeap, childHeap1);
                currentHeap = childHeap1;
                currentLevel -= 1;
            }else if(maxChild == childHeap2) {
                swap(g,array,currentHeap, childHeap2);
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
    public static void smoothSort(Graphics g,int[] array, int size) {
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
            smoothSortFix(g,array, i, toplevel, levels);
        }
        for(i = size - 2; i > 0; i--) {
            if(levels[toplevel] <= 1) {
                toplevel -= 1;
            } else {
                levels[toplevel] -= 1;
                levels[toplevel + 1] = levels[toplevel] - 1;
                toplevel += 1;

                smoothSortFix(g,array, i - leonardo[levels[toplevel]], toplevel - 1, levels);
                smoothSortFix(g,array, i, toplevel, levels);
            }
        }
    }

    //图形单位宽度
    private static int width = 40;
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
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        //清空矩形区域重新绘图
        g.clearRect(0,0,1900,1000);
    }
}
