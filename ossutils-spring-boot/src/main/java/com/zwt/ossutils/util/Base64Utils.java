package com.zwt.ossutils.util;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Base64图片转换工具类
 * @author zwt
 */
public class Base64Utils {

    /**
     * 本地图片转换成base64字符串
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgFile	图片本地路径
     * @return
     */
    public static String ImageToBase64ByLocal(String imgFile) {
        byte[] data = null;
        try(InputStream in = new FileInputStream(imgFile)){
            data = new byte[in.available()];
            in.read();
        }catch (IOException e){
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 在线图片转换成base64字符串
     * @param imgURL	图片线上路径
     * @return
     */
    public static String ImageToBase64ByOnline(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }

    /**
     * base64字符串转换成图片
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr		base64字符串
     * @param imgFilePath	图片存放路径
     * @return
     **/
    public static boolean Base64ToImage(String imgStr,String imgFilePath) {
        // 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            return false;
        }
        //如果包含 data:image/jpeg;base64, 前缀需要去掉
        if(imgStr.contains(",")){
            imgStr = imgStr.split(",")[1];
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try(OutputStream out = new FileOutputStream(imgFilePath)){
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * base64 字符串转字节
     * @param imgStr
     * @return
     */
    public static byte[] Base64ToByte(String imgStr){
        // 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            throw new RuntimeException("图片base64数据不能为空");
        }
        //如果包含 data:image/jpeg;base64, 前缀需要去掉
        if(imgStr.contains(",")){
            imgStr = imgStr.split(",")[1];
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return b;
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
