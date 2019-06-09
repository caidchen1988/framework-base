package com.zwt.ossutils.upload;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author zwt
 * @detail 华为云OBS
 * @date 2019/6/5
 * @since 1.0
 */
public class HuaweiOBSUploadUtil extends AbstractUploadUtil{


    public static final Logger logger = LoggerFactory.getLogger(AliOssUploadUtil.class);
    /**
     * 华为云accessKey
     */
    private String huaweiAccessKey;
    /**
     * 华为云secretKey
     */
    private String huaweiSecretKey;
    /**
     * 华为云endpoint
     */
    private String huaweiEndpoint;
    /**
     * 华为云bucket
     */
    private String huaweiBucket;

    /**
     * obsClient
     */
    private ObsClient obsClient;

    /**
     * 构造器
     * @param basedir
     * @param huaweiAccessKey
     * @param huaweiSecretKey
     * @param huaweiEndpoint
     * @param huaweiBucket
     */
    public HuaweiOBSUploadUtil(String basedir, String huaweiAccessKey, String huaweiSecretKey, String huaweiEndpoint, String huaweiBucket) {
        super(basedir);
        this.huaweiAccessKey = huaweiAccessKey;
        this.huaweiSecretKey = huaweiSecretKey;
        this.huaweiEndpoint = huaweiEndpoint;
        this.huaweiBucket = huaweiBucket;
        initClient();
    }

    @Override
    protected String upload(File tempFile, String realName) {
        initClient();
        PutObjectResult putObjectResult = obsClient.putObject(huaweiBucket, realName, tempFile);
        String urlString = putObjectResult.getObjectUrl();
        logger.info("华为云OBS上传服务------图片url:[{}]", urlString);
        return urlString;
    }

    @Override
    String upload(MultipartFile file) {
        initClient();
        String key = generateUploadFileName(file);
        try (InputStream is = new ByteArrayInputStream(file.getBytes())) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            // 上传
            PutObjectResult putObjectResult = obsClient.putObject(huaweiBucket,key, is, metadata);
            String urlString = putObjectResult.getObjectUrl();
            logger.info("华为云OBS上传服务------图片url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用华为云OBS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String upload(byte[] bytes, String contentType) {
        initClient();
        String realName = UUID.randomUUID().toString() + IMAGE_JPG;
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentLength((long)is.available());
            metadata.setContentType(contentType);
            // 上传
            PutObjectResult putObjectResult = obsClient.putObject(huaweiBucket,realName, is, metadata);
            String urlString = putObjectResult.getObjectUrl();
            logger.info("华为云OBS上传服务------图片url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用华为云OBS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initClient() {
        if(obsClient==null){
            ObsConfiguration obsConfiguration = new ObsConfiguration();
            obsConfiguration.setEndPoint(huaweiEndpoint);
            obsConfiguration.setConnectionTimeout(CONNECT_TIMEOUT);
            obsConfiguration.setSocketTimeout(UPLOAD_TIMEOUT);
            obsClient = new ObsClient(huaweiAccessKey,huaweiSecretKey,obsConfiguration);
        }
    }

    @Override
    protected void shutdown() {
        try {
            obsClient.close();
        }catch (IOException e){
            logger.error("关闭华为云OBSClient出现异常",e);
            throw new RuntimeException(e);
        }
    }
}
