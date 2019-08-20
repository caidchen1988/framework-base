package com.zwt.framework.utils.util.sort.help;

import javax.swing.*;
import java.awt.*;

/**
 * @author zwt
 * @detail
 * @date 2019/8/19
 * @since 1.0
 */
public class CocktailSortGIFMakeJFrame extends JFrame {

    public static void main(String[] args) {
        CocktailSortGIFMakeJFrame cocktailSortGIFMakeJFrame = new CocktailSortGIFMakeJFrame();
        CocktailSortGIFMakePanel cocktailSortGIFMakePanel = new CocktailSortGIFMakePanel();
        cocktailSortGIFMakeJFrame.add(cocktailSortGIFMakePanel);
        cocktailSortGIFMakeJFrame.setSize(700,800);
        cocktailSortGIFMakeJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cocktailSortGIFMakeJFrame.setVisible(true);
    }
    public CocktailSortGIFMakeJFrame(){

    }
}
class CocktailSortGIFMakePanel extends Panel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int [] array = new int[]{5,2,1,3,6,7,2,9,10,8,4};
        CocktailSortGIFMakeHelper.cocktailSort(g,array);
    }
}
class CocktailSortGIFMakeHelper {
    public static void cocktailSort(Graphics g,int[] src) {
        drawPicture(g,src,-1,-1);
        for (int i = 0; i < src.length / 2; i++) {
            //将最小值排到队首
            for (int j = i; j < src.length - i - 1; j++) {
                if (src[j] > src[j + 1]) {
                    drawPicture(g,src,j,j+1);
                    int temp = src[j];
                    src[j] = src[j + 1];
                    src[j + 1] = temp;
                    drawPicture(g,src,j,j+1);
                }else{
                    drawPicture(g,src,j,j+1);
                }
                drawPicture(g,src,-1,-1);
            }
            //将最大值排到队尾
            for (int j = src.length - 1 - (i + 1); j > i; j--) {
                if (src[j] < src[j - 1]) {
                    drawPicture(g,src,j,j-1);
                    int temp = src[j];
                    src[j] = src[j - 1];
                    src[j - 1] = temp;
                    drawPicture(g,src,j,j-1);
                }else{
                    drawPicture(g,src,j,j-1);
                }
                drawPicture(g,src,-1,-1);
            }
        }
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
