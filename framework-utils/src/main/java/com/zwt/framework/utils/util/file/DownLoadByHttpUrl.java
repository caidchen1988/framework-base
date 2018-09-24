package com.zwt.framework.utils.util.file;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zwt
 * @detail
 * @date 2018/9/20
 * @since 1.0
 */
public class DownLoadByHttpUrl {

    /**
     * 从网络Url中下载文件
     * @param urlStr 下载文件URL地址
     * @param fileName 下载后保存的文件名，支持中文
     * @param fileExt 文件名后缀
     * @param savePath 下载后文件的保存地址
     * @param charsetName 文件名的编码，本地传传null就可以，默认为 UTF-8,服务器传 ISO8859-1
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String fileExt,String savePath,String charsetName) throws IOException {
        FileOutputStream fos = null;
        InputStream inputStream = null;
        URL url = new URL(urlStr);
        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            inputStream = conn.getInputStream();

            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            //文件保存位置
            File saveDir = new File(savePath);
            if(!saveDir.exists()){
                saveDir.mkdirs();
            }
            if(StringUtils.isBlank(charsetName)){
                charsetName="UTF-8";
            }
            fileName = new String(fileName.getBytes("UTF-8"), charsetName);
            File file = new File(saveDir+File.separator+fileName+"."+fileExt);
            fos = new FileOutputStream(file);
            fos.write(getData);
        } catch (Exception e) {
            throw e;
        } finally {
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        System.out.println("\n下载地址:"+url+"    文件名："+fileName+"."+fileExt+"    保存地址："+savePath);
    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }

    public static void main(String[] args) {
    }


}
