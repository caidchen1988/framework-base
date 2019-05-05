package com.zwt.framework.utils.util.serializer;

import java.io.IOException;

/**
 * 对象序列化工具包
 *
 * @author zwt
 */
public class SerializationUtils {
    public static void main(String[] args) throws IOException {
        Apple apple =new Apple();
        apple.setColor("red");
        apple.setWeight(100);

        printData(new JavaSerializer(),apple);
        printData(new FSTSerializer(),apple);
        printData(new KryoSerializer(),apple);
        printData(new KryoPoolSerializer(),apple);
        printData(new JacksonSerializer(),apple);
        printData(new FastJsonSerializer(),apple);

    }
    public static void printData(Serializer serializer,Apple apple) throws IOException{
        long start = System.currentTimeMillis();
        byte[] bits = serializer.serialize(apple);
        System.out.println(serializer.name()+"序列化所需时间："+(System.currentTimeMillis()-start)+"ms");
        System.out.println(serializer.name()+"序列化后字节码长度："+bits.length);
        long start1 = System.currentTimeMillis();
        Object obj = serializer.deserialize(bits);
        System.out.println(serializer.name()+"反序列化所需时间："+(System.currentTimeMillis()-start1)+"ms");
        System.out.println(serializer.name()+"反序列化后对象："+obj.toString());
    }
}
