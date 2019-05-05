package com.zwt.framework.utils.util.serializer;

import java.io.*;

/**
 * @author zwt
 * @detail
 * @date 2019/4/24
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException,ClassNotFoundException{
        byte [] bytes = null;
        Apple apple = new Apple("red",150);
        //序列化
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)
         ){
            oos.writeObject(apple);
            bytes = baos.toByteArray();
            for (byte b : baos.toByteArray()) {
                System.out.print(Byte.toString(b) + " ");
            }
        }
        System.out.println();
        //反序列化
        try (
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais)
            ) {
            System.out.println(ois.readObject().toString());
        }
    }
}
