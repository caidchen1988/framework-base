package com.zwt.ossutils.upload;

import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author zwt
 * @detail 七牛云存储
 * @date 2019/6/4
 * @since 1.0
 */
public class QiNiuOSSUploadUtil extends AbstractUploadUtil{

    public static final Logger logger = LoggerFactory.getLogger(AliOssUploadUtil.class);
    /**
     * 七牛云accessKey
     */
    private String qiniuAccessKey;
    /**
     * 七牛云secretKey
     */
    private String qiniuSecretKey;
    /**
     * 七牛云endpoint
     */
    private String qiniuEndpoint;
    /**
     * 七牛云bucket
     */
    private String qiniuBucket;
    /**
     * 上传Manager
     */
    private UploadManager uploadManager;
    /**
     * 凭证
     */
    private String token;

    /**
     * 构造器
     * @param basedir
     * @param qiniuAccessKey
     * @param qiniuSecretKey
     * @param qiniuEndpoint
     * @param qiniuBucket
     */
    public QiNiuOSSUploadUtil(String basedir, String qiniuAccessKey, String qiniuSecretKey, String qiniuEndpoint, String qiniuBucket) {
        super(basedir);
        this.qiniuAccessKey = qiniuAccessKey;
        this.qiniuSecretKey = qiniuSecretKey;
        this.qiniuEndpoint = qiniuEndpoint;
        this.qiniuBucket = qiniuBucket;
        initClient();
    }

    @Override
    protected String upload(File tempFile, String realName) {
        initClient();
        try{
            Response response = uploadManager.put(tempFile,realName,token);
            if(!response.isOK()){
                throw new RuntimeException("使用七牛云上传文件失败");
            }
            return "http://" + qiniuEndpoint + "/" + qiniuBucket + "/" + realName;
        }catch (Exception e){
            logger.error("使用七牛云上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    String upload(MultipartFile file) {
        initClient();
        String key = generateUploadFileName(file);
        try (InputStream is = new ByteArrayInputStream(file.getBytes())){
            Response response = uploadManager.put(is,key,token,null,file.getContentType());
            if(!response.isOK()){
                throw new RuntimeException("使用七牛云上传文件失败");
            }
            return "http://" + qiniuEndpoint + "/" + qiniuBucket + "/" + key;
        }catch (IOException e){
            logger.error("使用七牛云上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String upload(byte[] bytes, String contentType) {
        initClient();
        String realName = UUID.randomUUID().toString() + IMAGE_JPG;
        try (InputStream is = new ByteArrayInputStream(bytes)){
            Response response = uploadManager.put(is,realName,token,null,contentType);
            if(!response.isOK()){
                throw new RuntimeException("使用七牛云上传文件失败");
            }
            return "http://" + qiniuEndpoint + "/" + qiniuBucket + "/" + realName;
        }catch (IOException e){
            logger.error("使用七牛云上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initClient() {
        if(uploadManager == null){
            Configuration configuration = new Configuration();
            configuration.connectTimeout = CONNECT_TIMEOUT;
            configuration.writeTimeout = UPLOAD_TIMEOUT;
            uploadManager = new UploadManager(configuration);
        }
        if(StringUtils.isBlank(token)){
            Auth auth = Auth.create(qiniuAccessKey, qiniuSecretKey);
            token = auth.uploadToken(qiniuBucket);
        }
    }

    @Override
    protected void shutdown() {
        uploadManager = null;
        token = null;
    }
}
