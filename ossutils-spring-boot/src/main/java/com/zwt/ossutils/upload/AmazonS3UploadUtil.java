package com.zwt.ossutils.upload;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author zwt
 * @detail 亚马逊S3文件上传
 * @date 2019/4/30
 * @since 1.0
 */
public class AmazonS3UploadUtil extends AbstractUploadUtil {
    /**
     * S3 accessKey
     */
    private String s3accessKey;
    /**
     *  S3 secretKey
     */
    private String s3secretKey;
    /**
     * S3 endpoint
     */
    private String s3endpoint;
    /**
     * S3 bucket
     */
    private String s3bucket;

    /**
     * 协议
     */
    private static Protocol protocol = Protocol.HTTP;

    /**
     * 亚马逊s3 client
     */
    private AmazonS3 client;

    /**
     * 构造器
     * @param basedir
     * @param s3accessKey
     * @param s3secretKey
     * @param s3endpoint
     * @param s3bucket
     */
    public AmazonS3UploadUtil(String basedir, String s3accessKey, String s3secretKey, String s3endpoint, String s3bucket) {
        super(basedir);
        this.s3accessKey = s3accessKey;
        this.s3secretKey = s3secretKey;
        this.s3endpoint = s3endpoint;
        this.s3bucket = s3bucket;
        initClient();
    }

    /**
     * 上传文件到 Amazon S3
     * @param tempFile
     * @param realName
     * @return
     */
    @Override
    protected String upload(File tempFile, String realName){
        try {
            initClient();
            client.setEndpoint(s3endpoint);
            client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
            client.putObject(new PutObjectRequest(s3bucket, realName, tempFile)
                    .withCannedAcl(CannedAccessControlList.AuthenticatedRead));
            String imageUrl = "http://" + s3endpoint + "/" + s3bucket + "/" + realName;
            logger.info("亚马逊S3上传服务------图片url:[{}]", imageUrl);
            return imageUrl;
        } catch (AmazonClientException e) {
            logger.error("使用亚马逊S3上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件到S3
     * @param file
     * @return
     */
    @Override
    String upload(MultipartFile file) {
        initClient();
        String key = generateUploadFileName(file);
        try (InputStream is = new ByteArrayInputStream(file.getBytes())){
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            PutObjectRequest mall = new PutObjectRequest(s3bucket, key, is, metadata)
                    .withCannedAcl(CannedAccessControlList.AuthenticatedRead);
            // 上传
            client.putObject(mall);
            return "http://" + s3endpoint + "/" + s3bucket + "/" + key;
        }catch (IOException e){
            logger.error("使用亚马逊S3上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试初始化S3Client
     */
    @Override
    protected void initClient() {
        if(client == null){
            AWSCredentials credential = new BasicAWSCredentials(s3accessKey, s3secretKey);
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setProtocol(protocol);
            clientConfig.setConnectionTimeout(CONNECT_TIMEOUT);
            clientConfig.setSocketTimeout(UPLOAD_TIMEOUT);
            client = new AmazonS3Client(credential, clientConfig);
        }
    }

    @Override
    protected String upload(byte[] bytes, String contentType) {
        initClient();
        String realName = UUID.randomUUID().toString() + IMAGE_JPG;
        try (InputStream is = new ByteArrayInputStream(bytes)){
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentLength(is.available());
            metadata.setContentType(contentType);
            PutObjectRequest mall = new PutObjectRequest(s3bucket, realName, is, metadata)
                    .withCannedAcl(CannedAccessControlList.AuthenticatedRead);
            // 上传
            client.putObject(mall);
            return "http://" + s3endpoint + "/" + s3bucket + "/" + realName;
        }catch (IOException e){
            logger.error("使用亚马逊S3上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void shutdown() {
        client = null;
    }
}
