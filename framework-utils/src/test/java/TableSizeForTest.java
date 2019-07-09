import java.util.Random;

/**
 * @author zwt
 * @detail 获取下一个最小的比入参n大的2的高次幂
 * @date 2019/7/3
 * @since 1.0
 */
public class TableSizeForTest {

    /**
     * 方法1
     * @param n
     * @return
     */
    public static int getNum1(int n){
        int m = 31;
        for(int i=0;i<= m;i++){
            int b = 1<<i;
            if(b > n){
                return b;
            }
        }
        return -1;
    }

    /**
     * 方法2
     * @param n
     * @return
     */
    public static int getNum2(int n){
        int m =31;
        for(int i=0;i<= m;i++){
            double b = Math.pow(2,i);
            if(b > n){
                return (int)b;
            }
        }
        return -1;
    }

    /**
     * 方法3
     * @param n
     * @return
     */
    public static int getNum3(int n){
        int k = 1;
        for(int i=0;i<= 31;i++){
            if(k > n){
                return k;
            }
            k *= 2;
        }
        return -1;
    }

    /**
     * 方法4
     * @param n
     * @return
     */
    public static int getNum4(int n){
        int i = 0;
        while (n > 0) {
            n = n >> 1;
            i++;
        }
        return 1<<i;
    }

    /**
     * 方法5
     * @param n
     * @return
     */
    public static int getNum5(int n){
        int i=1;
        while (n>1){
            n /=2;
            i++;
        }
        return 1<<i;
    }

    /**
     * 方法6
     * @param n
     * @return
     */
    public static int getNum6(int n){
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n < 0 ? 1 : n + 1;
    }

    public static void main(String[] args) {
        //生成若干数量的随机数，找到它们的最小的2的高次幂
        int [] a = new int[100000000];
        Random random = new Random();
        for (int i=0;i<a.length;i++){
            a[i] = random.nextInt(1073741824);
        }

        //方法1
        long start1 = System.currentTimeMillis();
        for (int i=0;i<a.length;i++){
            getNum1(a[i]);
        }
        System.out.println("方法1耗时:"+(System.currentTimeMillis()-start1)+"ms");
//        //方法2
//        long start2 = System.currentTimeMillis();
//        for (int i=0;i<a.length;i++){
//            getNum2(a[i]);
//        }
//        System.out.println("方法2耗时:"+(System.currentTimeMillis()-start2)+"ms");
        //方法3
        long start3 = System.currentTimeMillis();
        for (int i=0;i<a.length;i++){
            getNum3(a[i]);
        }
        System.out.println("方法3耗时:"+(System.currentTimeMillis()-start3)+"ms");
        //方法4
        long start4 = System.currentTimeMillis();
        for (int i=0;i<a.length;i++){
            getNum4(a[i]);
        }
        System.out.println("方法4耗时:"+(System.currentTimeMillis()-start4)+"ms");
        //方法5
        long start5 = System.currentTimeMillis();
        for (int i=0;i<a.length;i++){
            getNum5(a[i]);
        }
        System.out.println("方法5耗时:"+(System.currentTimeMillis()-start5)+"ms");
        //方法6
        long start6 = System.currentTimeMillis();
        for (int i=0;i<a.length;i++){
            getNum6(a[i]);
        }
        System.out.println("方法6耗时:"+(System.currentTimeMillis()-start6)+"ms");
    }
}

