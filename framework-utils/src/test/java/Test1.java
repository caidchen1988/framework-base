/**
 * @author zwt
 * @detail
 * @date 2018/9/14
 * @since 1.0
 */
public class Test1 {
    public static void main(String[] args) {
        int [] x=new int[]{5,10,333,6666,77777,123456,1234567,87654321,999999999,1111111111,Integer.MAX_VALUE};
        for(int i=0;i<x.length;i++){
            int a1=Test.stringSize1(x[i]);
            int a2=Test.stringSize2(x[i]);
            int a3=Test.myStringSize1(x[i]);
            int a4=Test.myStringSize2(x[i]);
            System.out.println(a1+"---"+a2+"---"+a3+"---"+a4);
        }
    }
}
