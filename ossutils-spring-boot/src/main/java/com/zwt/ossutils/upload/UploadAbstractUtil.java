package com.zwt.ossutils.upload;

import com.zwt.ossutils.util.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author zwt
 * @detail 上传文件抽象类
 * @date 2019/4/30
 * @since 1.0
 */
public abstract class UploadAbstractUtil {

    public static final Logger logger = LoggerFactory.getLogger(UploadAbstractUtil.class);

    /**
     * 文件缓存路径
     */
    private String basedir;

    public UploadAbstractUtil(String basedir) {
        this.basedir = basedir;
    }

    /**
     * 上传文件到云
     * @param tempFile
     * @param realName
     * @return
     */
    abstract String upload(File tempFile, String realName);

    /**
     * base64转为文件后在进行上传
     * @param base64Str
     * @return
     */
    public String base64Upload(String base64Str){
        String realName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(basedir + "/" + realName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Base64Utils.Base64ToImage(base64Str, file.getPath());
        String url;
        try {
            long startTime = System.currentTimeMillis();
            logger.debug("线程："+Thread.currentThread().getId()+",上传服务start------time:[{}]", startTime);
            url = upload(file, realName);
            long endTime = System.currentTimeMillis();
            logger.debug("线程："+Thread.currentThread().getId()+",上传服务end------time:[{}]", endTime);
            logger.info("线程："+Thread.currentThread().getId()+",上传服务耗时------time:[{}ms]", endTime-startTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败!");
        }
        if (file.exists()) {
            file.delete();
        }
        return url;
    }

    /**
     * 根据文件路径上传文件
     * @param filePath 文件路径
     * @return
     */
    public String fileUpload(String filePath){
        String url = null;
        try{
            File file = new File(filePath);
            if(!file.exists()){
              throw new RuntimeException("文件不存在！！");
            }
            String fileSuffix = filePath.substring(filePath.lastIndexOf("."));
            String realName = UUID.randomUUID().toString() + fileSuffix;
            long startTime = System.currentTimeMillis();
            logger.debug("线程："+Thread.currentThread().getId()+",上传服务start------time:[{}]", startTime);
            url = upload(file, realName);
            long endTime = System.currentTimeMillis();
            logger.debug("线程："+Thread.currentThread().getId()+",上传服务end------time:[{}]", endTime);
            logger.info("线程："+Thread.currentThread().getId()+",上传服务耗时------time:[{}ms]", endTime-startTime);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("文件上传失败！！",e);
        }
        return url;
    }
}
