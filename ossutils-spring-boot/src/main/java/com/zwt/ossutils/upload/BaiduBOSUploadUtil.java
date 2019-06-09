package com.zwt.ossutils.upload;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author zwt
 * @detail 百度云BOS
 * @date 2019/6/4
 * @since 1.0
 */
public class BaiduBOSUploadUtil extends AbstractUploadUtil {

    public static final Logger logger = LoggerFactory.getLogger(AliOssUploadUtil.class);
    /**
     * 百度云accessKey
     */
    private String baiduAccessKey;
    /**
     * 百度云secretKey
     */
    private String baiduSecretKey;
    /**
     * 百度云endpoint
     */
    private String baiduEndpoint;
    /**
     * 百度云bucket
     */
    private String baiduBucket;
    /**
     * 百度云 EndpointExternal
     */
    private String baiduEndpointExternal;

    /**
     * BosClient
     */
    private BosClient bosClient;

    /**
     * 构造器
     * @param basedir
     * @param baiduAccessKey
     * @param baiduSecretKey
     * @param baiduEndpoint
     * @param baiduBucket
     * @param baiduEndpointExternal
     */
    public BaiduBOSUploadUtil(String basedir, String baiduAccessKey, String baiduSecretKey, String baiduEndpoint, String baiduBucket, String baiduEndpointExternal) {
        super(basedir);
        this.baiduAccessKey = baiduAccessKey;
        this.baiduSecretKey = baiduSecretKey;
        this.baiduEndpoint = baiduEndpoint;
        this.baiduBucket = baiduBucket;
        this.baiduEndpointExternal = baiduEndpointExternal;
        initClient();
    }

    @Override
    protected String upload(File tempFile, String realName) {
        initClient();
        bosClient.putObject(baiduBucket,realName,tempFile);
        URL url =bosClient.generatePresignedUrl(baiduBucket, realName, 3600 * 24 * 365 * 100);
        String urlString = String.valueOf(url).split("\\?")[0];
        logger.info("百度云BOS上传服务------图片内网url:[{}]", urlString);
        urlString = urlString.replaceAll(baiduEndpoint, baiduEndpointExternal);
        logger.info("百度云BOS上传服务------图片外网url:[{}]", urlString);
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
            bosClient.putObject(baiduBucket,key, is, metadata);
            URL url = bosClient.generatePresignedUrl(baiduBucket, key, 3600 * 24 * 365 * 100);
            String urlString = String.valueOf(url).split("\\?")[0];
            logger.info("百度云BOS上传服务------图片内网url:[{}]", urlString);
            urlString = urlString.replaceAll(baiduEndpoint, baiduEndpointExternal);
            logger.info("百度云BOS上传服务------图片外网url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用百度云BOS上传文件出现异常",e);
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
            metadata.setContentLength(is.available());
            metadata.setContentType(contentType);
            // 上传
            bosClient.putObject(baiduBucket,realName, is, metadata);
            URL url = bosClient.generatePresignedUrl(baiduBucket, realName,3600 * 24 * 365 * 100);
            String urlString = String.valueOf(url).split("\\?")[0];
            logger.info("百度云BOS上传服务------图片内网url:[{}]", urlString);
            urlString = urlString.replaceAll(baiduEndpoint, baiduEndpointExternal);
            logger.info("百度云BOS上传服务------图片外网url:[{}]", urlString);
            return urlString;
        }catch (IOException e){
            logger.error("使用百度云BOS上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initClient() {
        if(bosClient==null){
            BosClientConfiguration bosClientConfiguration = new BosClientConfiguration ();
            bosClientConfiguration.setSocketTimeoutInMillis(UPLOAD_TIMEOUT);
            bosClientConfiguration.setConnectionTimeoutInMillis(CONNECT_TIMEOUT);
            bosClientConfiguration.setCredentials(new DefaultBceCredentials(baiduAccessKey,baiduSecretKey));
            bosClientConfiguration.setEndpoint(baiduEndpoint);
            bosClient = new BosClient(bosClientConfiguration);
        }
    }

    /**
     * shutdown Client
     */
    @Override
    public void shutdown(){
        bosClient.shutdown();
    }
}
