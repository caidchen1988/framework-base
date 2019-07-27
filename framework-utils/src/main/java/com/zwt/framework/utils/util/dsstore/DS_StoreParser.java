package com.zwt.framework.utils.util.dsstore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author: zwt
 * @Description: A Class for DS_StoreParser
 * @Name: DS_StoreParser
 * @Date: 2019/7/27 2:49 PM
 * @Version: 1.0
 */
public class DS_StoreParser {
    /**
     * Return bytes by reading .DS_Store File
     * Throw Exception if file not exist
     * Throw Exception if Reading Error
     * @Params: [fileName]
     * @Return: byte[]
     */
    public static byte[] readFile(String fileName){
        File file = new File(fileName);
        if((!file.exists())||(!file.isFile())){
            throw new RuntimeException(".DS_Store File not exist ！");
        }
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            byte[] b = new byte[1024];
            int len;
            while((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            return bos.toByteArray();
        }catch (IOException e){
            throw new RuntimeException("Reading .DS_Store File Error!"+e);
        }
    }

    public static void main(String[] args) {
        byte[] data = readFile("/Users/zhangwentong/Desktop/DS_Store/bak.DS_Store");
        DS_Store store = new DS_Store(data,true);

        List<String> files = store.traverseRoot();
        System.out.println("Count: "+ files.size());
        for(int i=0;i<files.size();i++){
            System.out.println(files.get(i));
        }
    }

}
