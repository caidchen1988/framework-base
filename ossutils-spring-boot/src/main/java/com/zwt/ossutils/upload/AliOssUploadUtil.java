package com.zwt.ossutils.upload;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * @author zwt
 * @detail 阿里云上传
 * @date 2019/4/30
 * @since 1.0
 */
public class AliOssUploadUtil extends AbstractUploadUtil {

    public static final Logger logger = LoggerFactory.getLogger(AliOssUploadUtil.class);
    /**
     * 阿里云accessKey
     */
    private String aliyunaccessKey;
    /**
     * 阿里云secretKey
     */
    private String aliyunsecretKey;
    /**
     * 阿里云endpoint
     */
    private String aliyunendpoint;
    /**
     * 阿里云endpointexternal
     */
    private String aliyunendpointexternal;
    /**
     * 阿里云bucket
     */
    private String aliyunbucket;

    /**
     * ossClient
     */
    private OSSClient ossClient;

    /**
     * 构造器
     * @param basedir
     * @param aliyunaccessKey
     * @param aliyunsecretKey
     * @param aliyunendpoint
     * @param aliyunendpointexternal
     * @param aliyunbucket
     */
    public AliOssUploadUtil(String basedir, String aliyunaccessKey, String aliyunsecretKey, String aliyunendpoint, String aliyunendpointexternal, String aliyunbucket) {
        super(basedir);
        this.aliyunaccessKey = aliyunaccessKey;
        this.aliyunsecretKey = aliyunsecretKey;
        this.aliyunendpoint = aliyunendpoint;
        this.aliyunendpointexternal = aliyunendpointexternal;
        this.aliyunbucket = aliyunbucket;
        initClient();
    }

    /**
     * 阿里云文件上传
     * @param tempFile
     * @param realName
     * @return
     */
    @Override
    protected String upload(File tempFile,String realName){
        initClient();
        ossClient.putObject(aliyunbucket, realName, tempFile);
        URL url = ossClient.generatePresignedUrl(aliyunbucket, realName, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
        String urlString = String.valueOf(url).split("\\?")[0];
        logger.info("阿里云OSS上传服务------图片内网url:[{}]", urlString);
        urlString = urlString.replaceAll(aliyunendpoint, aliyunendpointexternal);
        logger.info("阿里云OSS上传服务------图片外网url:[{}]", urlString);
        return urlString;
    }

    /**
     * 阿里云文件上传
     * @param file
     * @return
     */
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
            ossClient.putObject(aliyunbucket,key, is, metadata);
            URL url = ossClient.generatePresignedUrl(aliyunbucket, key, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
            String urlString = String.valueOf(url).split("\\?")[0];
            logger.info("阿里云OSS上传服务------图片内网url:[{}]", urlString);
            urlString = urlString.replaceAll(aliyunendpoint, aliyunendpointexternal);
            logger.info("阿里云OSS上传服务------图片外网url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用阿里云OSS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试初始化client
     */
    @Override
    protected void initClient() {
        if(ossClient == null){
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setConnectionTimeout(CONNECT_TIMEOUT);
            clientConfiguration.setSocketTimeout(UPLOAD_TIMEOUT);
            ossClient = new OSSClient(aliyunendpoint, aliyunaccessKey, aliyunsecretKey,clientConfiguration);
        }
    }

    @Override
    protected String upload(byte[] bytes, String contentType) {
        initClient();
        String realName = UUID.randomUUID().toString() + IMAGE_JPG;
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentLength(is.available());
            metadata.setContentType(contentType);
            // 上传
            ossClient.putObject(aliyunbucket,realName, is, metadata);
            URL url = ossClient.generatePresignedUrl(aliyunbucket, realName, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
            String urlString = String.valueOf(url).split("\\?")[0];
            logger.info("阿里云OSS上传服务------图片内网url:[{}]", urlString);
            urlString = urlString.replaceAll(aliyunendpoint, aliyunendpointexternal);
            logger.info("阿里云OSS上传服务------图片外网url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用阿里云OSS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * shutdown Client
     */
    @Override
    public void shutdown(){
        ossClient.shutdown();
    }
}
