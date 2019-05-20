package com.zwt.ossutils.upload;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

/**
 * @author zwt
 * @detail 亚马逊S3文件上传
 * @date 2019/4/30
 * @since 1.0
 */
public class AmazonS3UploadUtil extends UploadAbstractUtil{
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
     * S3 Client
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
    }

    /**
     * 上传文件到 Amazon S3
     * @param tempFile
     * @param realName
     * @return
     */
    @Override
    public String upload(File tempFile, String realName){
        try {
            if(client == null){
                AWSCredentials credential = new BasicAWSCredentials(s3accessKey, s3secretKey);
                ClientConfiguration clientConfig = new ClientConfiguration();
                clientConfig.setProtocol(protocol);
                client = new AmazonS3Client(credential, clientConfig);
            }
            client.setEndpoint(s3endpoint);
            client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
            client.putObject(new PutObjectRequest(s3bucket, realName, tempFile)
                    .withCannedAcl(CannedAccessControlList.AuthenticatedRead));
            String imageUrl = "http://" + s3endpoint + "/" + s3bucket + "/" + realName;
            logger.info("亚马逊S3上传服务------图片url:[{}]", imageUrl);
            return imageUrl;
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
        }
        return null;
    }
}
