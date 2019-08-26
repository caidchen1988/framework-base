package com.zwt.framework.utils.util.sort.help;

import javax.swing.*;
import java.awt.*;

/**
 * @author zwt
 * @detail
 * @date 2019/8/19
 * @since 1.0
 */
public class PancakeSortGIFMakeJFrame extends JFrame {
    public static void main(String[] args) {
        PancakeSortGIFMakeJFrame pancakeSortGIFMakeJFrame = new PancakeSortGIFMakeJFrame();
        PancakeSortGIFMakePanel pancakeSortGIFMakePanel = new PancakeSortGIFMakePanel();
        pancakeSortGIFMakeJFrame.add(pancakeSortGIFMakePanel);
        pancakeSortGIFMakeJFrame.setSize(700,800);
        pancakeSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pancakeSortGIFMakeJFrame.setVisible(true);
    }
    public PancakeSortGIFMakeJFrame(){
    }
}

class PancakeSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int [] array = new int[]{5,2,1,3,6,7,2,9,10,8,4};
        PancakeSortGIFMakeHelper.pancakeSort(g,array);
    }
}

class PancakeSortGIFMakeHelper {
    private static void flip(Graphics g,int n, int[] heap) {
        for (int i = 0; i < (n + 1) / 2; ++i) {
            drawPicture(g,heap,i,n-i);
            int tmp = heap[i];
            heap[i] = heap[n - i];
            heap[n - i] = tmp;
            drawPicture(g,heap,i,n-i);
        }
    }

    /**
     * 获取最小最大值
     *
     * @param n
     * @param heap
     * @return
     */
    private static int[] minmax(int n, int[] heap) {
        int xm, xM;
        xm = xM = heap[0];
        int posm = 0, posM = 0;

        for (int i = 1; i < n; ++i) {
            if (heap[i] < xm) {
                xm = heap[i];
                posm = i;
            } else if (heap[i] > xM) {
                xM = heap[i];
                posM = i;
            }
        }
        return new int[]{posm, posM};
    }

    /**
     * 排序
     *
     * @param n
     * @param dir
     * @param heap
     */
    private static void sort(Graphics g,int n, int dir, int[] heap) {
        if (n == 0) {
            return;
        }
        int[] mM = minmax(n, heap);
        int bestXPos = mM[dir];
        int altXPos = mM[1 - dir];
        boolean flipped = false;

        if (bestXPos == n - 1) {
            --n;
        } else if (bestXPos == 0) {
            flip(g,n - 1, heap);
            --n;
        } else if (altXPos == n - 1) {
            dir = 1 - dir;
            --n;
            flipped = true;
        } else {
            flip(g,bestXPos, heap);
        }
        sort(g,n, dir, heap);
        if (flipped) {
            flip(g,n, heap);
        }
    }

    /**
     * pancakeSort主入口
     *
     * @param array
     */
    public static void pancakeSort(Graphics g,int[] array) {
        drawPicture(g,array,-1,-1);
        sort(g,array.length, 1, array);
        drawPicture(g,array,-1,-1);
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
