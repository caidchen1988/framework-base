package com.zwt.ossutils.config;

import com.zwt.ossutils.upload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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

    @Autowired
    private Environment environment;

    /**
     * 亚马逊S3配置
     */
    private String s3accessKey;
    private String s3secretKey;
    private String s3endpoint;
    private String s3bucket;

    /**
     * 阿里云OSS配置
     */
    private String aliyunAccessKey;
    private String aliyunSecretKey;
    private String aliyunBucket;
    private String aliyunEndpoint;
    private String aliyunEndpointexternal;

    /**
     * 微软Azure配置
     */
    private String azureAccountName;
    private String azureAccountKey;
    private String azureEndpointSuffix;
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
            //亚马逊s3
            case AMAZON:
                s3accessKey = environment.getRequiredProperty("s3.accessKey");
                s3secretKey = environment.getRequiredProperty("s3.secretKey");
                s3endpoint = environment.getRequiredProperty("s3.endpoint");
                s3bucket = environment.getRequiredProperty("s3.bucket");
                uploadAbstractUtil = new AmazonS3UploadUtil(basedir,s3accessKey,s3secretKey,s3endpoint,s3bucket);
                return uploadAbstractUtil;
            //阿里云OSS
            case ALIOSS:
                aliyunAccessKey = environment.getRequiredProperty("aliyun.accessKey");
                aliyunSecretKey = environment.getRequiredProperty("aliyun.secretKey");
                aliyunBucket = environment.getRequiredProperty("aliyun.bucket");
                aliyunEndpoint = environment.getRequiredProperty("aliyun.endpoint");
                aliyunEndpointexternal = environment.getRequiredProperty("aliyun.endpointexternal");
                uploadAbstractUtil = new AliOssUploadUtil(basedir,aliyunAccessKey,aliyunSecretKey,aliyunEndpoint,aliyunEndpointexternal,aliyunBucket);
                return uploadAbstractUtil;
            //微软Azure
            case AZURE:
                azureAccountName = environment.getRequiredProperty("azure.accountName");
                azureAccountKey = environment.getRequiredProperty("azure.accountKey");
                azureEndpointSuffix = environment.getRequiredProperty("azure.endpointSuffix");
                azureContainerName = environment.getRequiredProperty("azure.containerName");
                uploadAbstractUtil = new AzureUploadUtil(basedir,azureAccountName,azureAccountKey,azureEndpointSuffix,azureContainerName);
                return uploadAbstractUtil;
            default:
                throw new RuntimeException("暂不支持其他类型的云上传！！！");
        }
    }
}
