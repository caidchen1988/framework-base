package com.zwt.ossutils.upload;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
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
 * @Author: zwt
 * @Description: 腾讯云COS上传
 * @Name: TencentCOSUploadUtil
 * @Date: 2019/6/2 9:28 AM
 * @Version: 1.0
 */
public class TencentCOSUploadUtil extends UploadAbstractUtil {

    public static final Logger logger = LoggerFactory.getLogger(TencentCOSUploadUtil.class);
    /**
     * 腾讯云COS AccessKey
     */
    private String qAccessKey;
    /**
     * 腾讯云COS SecretKey
     */
    private String qSecretKey;
    /**
     * 腾讯云COS bucket
     */
    private String qBucket;
    /**
     * 腾讯云COS region
     */
    private String qRegion;
    /**
     * 腾讯云COS qEndpoint
     */
    private String qEndpoint;

    /**
     * 腾讯云COS qEndpointExternal
     */
    private String qEndpointExternal;

    /**
     * COSClient
     */
    private COSClient cosClient;

    public TencentCOSUploadUtil(String basedir, String qAccessKey, String qSecretKey, String qBucket, String qRegion, String qEndpoint, String qEndpointExternal) {
        super(basedir);
        this.qAccessKey = qAccessKey;
        this.qSecretKey = qSecretKey;
        this.qBucket = qBucket;
        this.qRegion = qRegion;
        this.qEndpoint = qEndpoint;
        this.qEndpointExternal = qEndpointExternal;
    }

    @Override
    protected String upload(File tempFile, String realName) {
        initClient();
        cosClient.putObject(qBucket,realName,tempFile);
        URL url =cosClient.generatePresignedUrl(qBucket, realName, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
        String urlString = String.valueOf(url).split("\\?")[0];
        logger.info("腾讯云COS上传服务------图片内网url:[{}]", urlString);
        urlString = urlString.replaceAll(qEndpoint, qEndpointExternal);
        logger.info("腾讯云COS上传服务------图片外网url:[{}]", urlString);
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
            cosClient.putObject(qBucket,key, is, metadata);
            URL url = cosClient.generatePresignedUrl(qBucket, key, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
            String urlString = String.valueOf(url).split("\\?")[0];
            logger.info("腾讯云COS上传服务------图片内网url:[{}]", urlString);
            urlString = urlString.replaceAll(qEndpoint, qEndpointExternal);
            logger.info("腾讯云COS上传服务------图片外网url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用腾讯云COS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String upload(byte[] bytes, String contentType) {
        initClient();
        String realName = UUID.randomUUID().toString() + ".jpg";
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentLength(is.available());
            metadata.setContentType(contentType);
            // 上传
            cosClient.putObject(qBucket,realName, is, metadata);
            URL url = cosClient.generatePresignedUrl(qBucket, realName, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
            String urlString = String.valueOf(url).split("\\?")[0];
            logger.info("腾讯云COS上传服务------图片内网url:[{}]", urlString);
            urlString = urlString.replaceAll(qEndpoint, qEndpointExternal);
            logger.info("腾讯云COS上传服务------图片外网url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用腾讯云COS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 初始化COSCilent
     * @Date: 2019/6/2 9:42 AM
     * @Params: []
     * @Return: void
     */
    @Override
    protected void initClient() {
        if(cosClient==null){
            COSCredentials cosCredentials = new BasicCOSCredentials(qAccessKey,qSecretKey);
            ClientConfig clientConfig = new ClientConfig(new Region(qRegion));
            cosClient = new COSClient(cosCredentials,clientConfig);
        }
    }

    /**
     * shutdown Client
     */
    public void shutdown(){
        cosClient.shutdown();
    }
}
