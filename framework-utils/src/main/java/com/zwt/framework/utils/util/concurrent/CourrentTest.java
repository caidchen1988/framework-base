package com.zwt.framework.utils.util.concurrent;

/**
 * @author zwt
 * @detail
 * @date 2019/4/16
 * @since 1.0
 */
public class CourrentTest implements ProducerTemplate<String>, ConsumerTemplate<String>{
    @Override
    public void production(Context<String> context) throws Exception {
        //1. 组装数据
        //数据库查询、组装数据过程略，由for循环插入数据代替
        for(int i = 0;i<1000;i++){
            Thread.sleep(10);
            //插入不成功，说明可能是消费者线程死亡或者队列已满
            if(!context.offerDataToConsumptionQueue(i+"")){
                return;
            }
        }
    }
    @Override
    public void consumption(Context<String> context) throws Exception {
        //2. 上传数据/文件
        //消费者消费数据
        while (true) {
            String str = context.pollDataFromConsumptionQueue();
            if (str == null) {
                break;
            }
            //假设每个文件上传消耗1s时间
            Thread.sleep(1000);
            System.out.println(str);
        }
    }
    //测试
    public static void main(String[] args) throws Exception{
        CourrentTest courrentTest = new CourrentTest();
        new Coordinator(new Context<String>(),10).start(courrentTest,courrentTest);
    }
}
