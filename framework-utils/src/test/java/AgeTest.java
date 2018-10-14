/**
 * @author zwt
 * @detail
 * @date 2018/9/27
 * @since 1.0
 */
public class AgeTest {
    static int getAge(int n){
        if(n==1){
            return 8;
        }else{
            return getAge(n-1)+2;
        }
    }

    public static void main(String[] args) {
        System.out.println(getAge1(1000000,8));
    }


    /**
     *
     *
     * getAge(1)=8
     * getAge(2)=getAge(1)+2=10
     * getAge(3)=getAge(2)+2=12
     * getAge(4)=getAge(3)+2=14
     *
     *
     *
     *
     *
     *   getAge(4,8)
     * = getAge(3,8+2)
     * = getAge(2,8+2+2)
     * = getAge(1,8+2+2+2)
     * = 14
     */


    static int getAge1(int n,int result){
        if(n==1){
            return result;
        }else{
            return getAge1(n-1,result+2);
        }
    }


    /**
     *  require n>=1
     * @param n
     * @param result
     * @param step
     * @return
     */
    static int getAge2(int n,int result,int step){
        while(n>1){
            result+=step;
            n--;
        }
        return result;
    }

    static int getAge3(int start,int end,int firstValue,int step){
        for(int i=start;i<end;i++){
            firstValue+=step;
        }
        return firstValue;
    }

    static int getAge4(int start,int end,int firstValue,int step){
        return firstValue+(end-start)*step;
    }

}
