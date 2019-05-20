package com.zwt.ossutils.config;

import com.zwt.ossutils.upload.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zwt
 * @detail oss相关帮助类
 * @date 2019/5/9
 * @since 1.0
 */
@Configuration
public class OssUtilsConfiguration {
    @Value("${file.cache.dir}")
    private String basedir;

    @Value("${file.upload.server.type}")
    private String uploadServerType;

    @Value("${s3.accessKey}")
    private String s3accessKey;

    @Value("${s3.secretKey}")
    private String s3secretKey;

    @Value("${s3.endpoint}")
    private String s3endpoint;

    @Value("${s3.bucket}")
    private String s3bucket;

    @Value("${aliyun.accessKey}")
    private String aliyunAccessKey;

    @Value("${aliyun.secretKey}")
    private String aliyunSecretKey;

    @Value("${aliyun.bucket}")
    private String aliyunBucket;

    @Value("${aliyun.endpoint}")
    private String aliyunEndpoint;

    @Value("${aliyun.endpointexternal}")
    private String aliyunEndpointexternal;

    @Value("${azure.accountName}")
    private String azureAccountName;

    @Value("${azure.accountKey}")
    private String azureAccountKey;

    @Value("${azure.endpointSuffix}")
    private String azureEndpointSuffix;

    @Value("${azure.containerName}")
    private String azureContainerName;

    /**
     * 根据配置的 file.upload.server.type 选择一个上传服务器
     * @return
     */
    @Bean
    public UploadAbstractUtil uploadAbstractUtil(){
        //可以根据枚举进行配置 使用阿里云或者亚马逊S3或者Azure
        UploadServerEnum uploadServerEnum = UploadServerEnum.getEnum(uploadServerType);
        UploadAbstractUtil uploadAbstractUtil;
        switch (uploadServerEnum){
            case AMAZON:
                uploadAbstractUtil = new AmazonS3UploadUtil(basedir,s3accessKey,s3secretKey,s3endpoint,s3bucket);
                return uploadAbstractUtil;
            case ALIOSS:
                uploadAbstractUtil = new AliOssUploadUtil(basedir,aliyunAccessKey,aliyunSecretKey,aliyunEndpoint,aliyunEndpointexternal,aliyunBucket);
                return uploadAbstractUtil;
            case AZURE:
                uploadAbstractUtil = new AzureUploadUtil(basedir,azureAccountName,azureAccountKey,azureEndpointSuffix,azureContainerName);
                return uploadAbstractUtil;
            default:
                throw new RuntimeException("暂不支持其他类型的云上传！！！");
        }
    }
}
