package com.zwt.framework.utils.util.sort.help;

import javax.swing.*;
import java.awt.*;

/**
 * @author zwt
 * @detail
 * @date 2019/8/19
 * @since 1.0
 */
public class BinaryInsertionSortGIFMakeJFrame extends JFrame {
    public static void main(String[] args) {
        BinaryInsertionSortGIFMakeJFrame binaryInsertionSortGIFMakeJFrame = new BinaryInsertionSortGIFMakeJFrame();
        BinaryInsertionSortGIFMakePanel binaryInsertionSortGIFMakePanel = new BinaryInsertionSortGIFMakePanel();
        binaryInsertionSortGIFMakeJFrame.add(binaryInsertionSortGIFMakePanel);
        binaryInsertionSortGIFMakeJFrame.setSize(1200,800);
        binaryInsertionSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        binaryInsertionSortGIFMakeJFrame.setVisible(true);
    }
    public BinaryInsertionSortGIFMakeJFrame(){
    }
}

class BinaryInsertionSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int [] array = new int[]{5,2,1,3,6,7,2,9,10,8,4};
        BinaryInsertionSortGIFMakeHelper.binaryInsertionSort(g,array);
    }
}

class BinaryInsertionSortGIFMakeHelper {

    public static void binaryInsertionSort(Graphics g,int[] data) {
        drawPicture(g,data,-1,-1);
        for (int i = 1, len = data.length; i < len; i++) {
            // 要插入的元素
            int temp = data[i];
            drawPicture(g,data,-1,i);
            int low = 0;
            int high = i - 1;
            // 折半比较，直到找到low大于high时（找到比他大的值的位置low）
            while(low <= high) {
                int mid = (low+high)/2;
                if (data[mid] > temp) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            drawPicture(g,data,i,low);
            // 移动 比他大的值，全部后移
            for (int j = i; j > low; j--) {
                data[j] = data[j-1];
            }
            // 插入
            data[low] = temp;
            drawPicture(g,data,-1,-1);
        }
    }

    //图形单位宽度
    private static int width = 50;
    //图形单位高度
    private static int baseHeight = 50;


    /**
     * 画图 数组
     * @param g
     * @param array 数组
     * @param tempIdx  如果不为-1，会将这个值放到 tempX，tempY处，且为红色，否则在原处为蓝色
     * @param idx     当前下标
     */
    public static void drawPicture(Graphics g, int[] array, int tempIdx, int idx){
        //水印，没什么用
        int waterX = 400;
        int waterY = 700;
        g.setColor(Color.gray);
        g.drawString("www.sakuratears.top",waterX,waterY);

        //数据临时存放的地方
        int tempX = 1000;
        int tempY = 200;


        //绘制矩形
        int startX = 100;
        int startY = 650;


        for(int i=0;i<array.length;i++){
            if(i==tempIdx){
                g.setColor(Color.RED);
                g.fillRect(tempX,tempY,width,baseHeight * array[tempIdx]);
                g.drawString(array[tempIdx]+"",tempX+width/2,tempY-width/2);
                startX += width;
            }else if(idx == i){
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
        g.clearRect(0,0,1200,800);
    }
}
