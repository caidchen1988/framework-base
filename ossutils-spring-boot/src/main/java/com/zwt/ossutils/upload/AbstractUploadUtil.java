package com.zwt.ossutils.upload;

import com.zwt.ossutils.util.Base64Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author zwt
 * @detail 上传文件抽象类
 * @date 2019/4/30
 * @since 1.0
 */
public abstract class AbstractUploadUtil {

    public static final Logger logger = LoggerFactory.getLogger(AbstractUploadUtil.class);

    /**
     * 图片类型，jpg
     */
    protected static final String IMAGE_JPG = ".jpg";
    /**
     * 上传超时时间 20s
     */
    protected static final int UPLOAD_TIMEOUT = 20000;
    /**
     * 建立连接超时时间
     */
    protected static final int CONNECT_TIMEOUT = 20000;

    /**
     * 文件缓存路径
     */
    private String basedir;

    public AbstractUploadUtil(String basedir) {
        this.basedir = basedir;
    }

    /**
     * 上传文件到云
     * @param tempFile
     * @param realName
     * @return
     */
    protected abstract String upload(File tempFile, String realName);

    /**
     * 上传文件到云
     * @param file
     * @return
     */
    abstract String upload(MultipartFile file);

    /**
     * 上传文件到云
     * @param bytes
     * @param contentType
     * @return
     */
    protected abstract String upload(byte[] bytes,String contentType);

    /**
     * 尝试初始化客户端
     */
    protected abstract void initClient();

    /**
     * 生成一个唯一的上传文件名
     * @param file
     * @return
     */
    protected String generateUploadFileName(MultipartFile file){
        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        // 生成唯一的key
        return uuid + ext;
    }

    /**
     * base64转为文件后在进行上传
     * @param base64Str
     * @return
     */
    public String base64UploadUseTempFile(String base64Str){
        String realName = UUID.randomUUID().toString() + IMAGE_JPG;
        File file = new File(basedir + "/" + realName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e){
                logger.error("文件上传，尝试创建文件时失败！！！",e);
                throw new RuntimeException("文件上传失败！！！");
            }
        }

        boolean flag = Base64Utils.Base64ToImage(base64Str, file.getPath());
        if(!flag){
            throw new RuntimeException("base64转换为文件时发生错误！！！");
        }
        String url;
        try {
            long startTime = System.currentTimeMillis();
            url = upload(file, realName);
            logger.info("tempFile---上传服务耗时------time:[{}ms]", System.currentTimeMillis()-startTime);
        } catch (Exception e) {
            logger.error("文件上传，上传文件时发生异常！！！",e);
            throw new RuntimeException("文件上传失败!");
        }
        if (file.exists()) {
            file.delete();
        }
        return url;
    }

    /**
     * 通用文件上传
     * @param filePath 文件路径
     * @return
     */
    public String fileUpload(String filePath){
        if(StringUtils.isBlank(filePath)){
           throw new RuntimeException("请输入正确的文件路径！");
        }
        File file = new File(filePath);
        if(!file.exists()){
            throw new RuntimeException("请输入正确的文件路径！");
        }
        int position = filePath.lastIndexOf(".");
        String fileSuffix = "";
        if(position > 0){
            fileSuffix = filePath.substring(position);
        }
        //上传到云上的文件名
        String realName = UUID.randomUUID().toString() + fileSuffix;
        String url;
        try {
            long startTime = System.currentTimeMillis();
            url = upload(file, realName);
            logger.info("filePath---上传服务耗时------time:[{}ms]", System.currentTimeMillis()-startTime);
        } catch (Exception e) {
            logger.error("文件上传，上传文件时发生异常！！！",e);
            throw new RuntimeException("文件上传失败!");
        }
        return url;
    }


    /**
     * 使用流来进行文件上传
     * @param base64Str
     * @return
     */
    public String base64UploadUseInputStream(String base64Str){
        byte[] bytes = Base64Utils.Base64ToByte(base64Str);
        String url;
        try {
            long startTime = System.currentTimeMillis();
            url = upload(bytes,"image/jpeg");
            logger.info("Stream---上传服务耗时------time:[{}ms]", System.currentTimeMillis()-startTime);
        } catch (Exception e) {
            logger.error("文件上传，上传文件时发生异常！！！",e);
            throw new RuntimeException("文件上传失败!");
        }
        return url;
    }

    /**
     * shutdown client
     */
    protected abstract void shutdown();

}
