import com.zwt.framework.utils.util.excel.WriteExcelUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Random;

/**
 * @author zwt
 * @detail
 * @date 2018/9/12
 * @since 1.0
 */
public class Test {

    /**
     * JDK里的位数算法
     */
    final static int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE };

    // Requires positive x
    static int stringSize1(int x) {
        for (int i=0; ; i++)
            if (x <= sizeTable[i])
                return i+1;
    }


    /**
     * 用乘法计算位数
     * @param x
     * @return
     */
    // Requires positive x
    static int stringSize2(int x) {
        int p = 10;
        for (int i=1; i<11; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 10;
    }


    /**
     * 用String方法计算位数
     * @param x
     * @return
     */
    static int myStringSize1(int x){
        return String.valueOf(x).length();
    }

    /**
     * 用除法计算位数
     * @param x
     * @return
     */
    static int myStringSize2(int x){
        int num=1;
        while(x>10){
            x=x/10;
            num++;
        }
        return num;
    }

    public static void main(String[] args) throws  Exception{
//        int m=1234567;
//        System.out.println(stringSize1(m));
//        System.out.println(stringSize2(m));
//        System.out.println(myStringSize1(m));
//        System.out.println(myStringSize2(m));

        List<List<Long>> rowList= Lists.newArrayList();

        List<String> titleList=Lists.newArrayList();
        titleList.add("JDK方法");
        titleList.add("乘法");
        titleList.add("String方法");
        titleList.add("除法");


        for(int s=0;s<50;s++){

            System.gc();
            Thread.sleep(2000);

            List<Long> cellList=Lists.newArrayList();

            int [] xArrays=new int [1000];
            Random random=new Random();
            for(int i=0;i<xArrays.length;i++){
                xArrays[i]=random.nextInt();
            }


            System.gc();
            Thread.sleep(2000);
            long start1=System.nanoTime();
            for(int i=0;i<xArrays.length;i++){
                stringSize1(xArrays[i]);
            }
            long end1=System.nanoTime();
            long time1=(end1-start1)/1000;
            System.out.println("JDK方法耗时---》"+time1+"ms");
            cellList.add(time1);



            System.gc();
            Thread.sleep(2000);
            long start4=System.nanoTime();
            for(int i=0;i<xArrays.length;i++){
                stringSize2(xArrays[i]);
            }
            long end4=System.nanoTime();
            long time4=(end4-start4)/1000;
            System.out.println("乘法耗时---》"+time4+"ms");
            cellList.add(time4);



            System.gc();
            Thread.sleep(2000);
            long start2=System.nanoTime();
            for(int i=0;i<xArrays.length;i++){
                myStringSize1(xArrays[i]);
            }
            long end2=System.nanoTime();
            long time2=(end2-start2)/1000;
            System.out.println("String方法耗时---》"+time2+"ms");
            cellList.add(time2);



            System.gc();
            Thread.sleep(2000);
            long start3=System.nanoTime();
            for(int i=0;i<xArrays.length;i++){
                myStringSize2(xArrays[i]);
            }
            long end3=System.nanoTime();
            long time3=(end3-start3)/1000;
            System.out.println("除法耗时---》"+time3+"ms");
            cellList.add(time3);

            rowList.add(cellList);
        }

        WriteExcelUtil.writeExecl(titleList,rowList,"C:\\Users\\DELL-3020\\Desktop\\workbook.xlsx");
    }
}
