package com.zwt.framework.utils.util.sort.help;

import javax.swing.*;
import java.awt.*;

/**
 * @author zwt
 * @detail
 * @date 2019/8/19
 * @since 1.0
 */
public class InPlaceMergeSortGIFMakeJFrame extends JFrame {
    public static void main(String[] args) {
        InPlaceMergeSortGIFMakeJFrame inPlaceMergeSortGIFMakeJFrame = new InPlaceMergeSortGIFMakeJFrame();
        InPlaceMergeSortGIFMakePanel inPlaceMergeSortGIFMakePanel = new InPlaceMergeSortGIFMakePanel();
        inPlaceMergeSortGIFMakeJFrame.add(inPlaceMergeSortGIFMakePanel);
        inPlaceMergeSortGIFMakeJFrame.setSize(700,800);
        inPlaceMergeSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inPlaceMergeSortGIFMakeJFrame.setVisible(true);
    }
    public InPlaceMergeSortGIFMakeJFrame(){
    }
}

class InPlaceMergeSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int [] array = new int[]{5,2,1,3,6,7,2,9,10,8,4};
        InPlaceMergeSortGIFMakeHelper.inPlaceMergeSort(g,array);
    }
}

class InPlaceMergeSortGIFMakeHelper {
    public static void inPlaceMergeSort(Graphics g,int[] array) {
        drawPicture(g,array,-1,-1);
        inPlaceMergeSort(g,array, 0, array.length - 1);
        drawPicture(g,array,-1,-1);
    }

    private static void inPlaceMergeSort(Graphics g,int[] array, int first, int last) {
        int mid, lt, rt;
        int tmp;
        if (first >= last) {
            return;
        }
        mid = (first + last) / 2;
        inPlaceMergeSort(g,array, first, mid);
        inPlaceMergeSort(g,array, mid + 1, last);
        lt = first;
        rt = mid + 1;
        // One extra check:  can we SKIP the merge?
        if (array[mid] <= array[rt]) {
            return;
        }
        while (lt <= mid && rt <= last) {
            // Select from left:  no change, just advance lt
            if (array[lt] <= array[rt]) {
                lt++;
                // Select from right:  rotate [lt..rt] and correct
            } else {
                // Will move to [lt]
                drawPicture(g,array,lt,rt);
                tmp = array[rt];
                System.arraycopy(array, lt, array, lt + 1, rt - lt);
                array[lt] = tmp;
                drawPicture(g,array,lt,rt);
                // EVERYTHING has moved up by one
                lt++;
                mid++;
                rt++;
            }
        }
        // Whatever remains in [rt..last] is in place
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
