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
     * 腾讯云COS配置
     */
    private String qAccessKey;
    private String qSecretKey;
    private String qBucket;
    private String qRegion;
    private String qEndpoint;
    private String qEndpointExternal;

    /**
     * 百度云BOS配置
     */
    private String baiduAccessKey;
    private String baiduSecretKey;
    private String baiduEndpoint;
    private String baiduBucket;
    private String baiduEndpointExternal;

    /**
     * 七牛云存储配置
     */
    private String qiniuAccessKey;
    private String qiniuSecretKey;
    private String qiniuEndpoint;
    private String qiniuBucket;

    /**
     * 华为云OBS配置
     */
    private String huaweiAccessKey;
    private String huaweiSecretKey;
    private String huaweiEndpoint;
    private String huaweiBucket;

    /**
     * 根据配置的 file.upload.server.type 选择一个上传服务器
     * @return
     */
    @Bean
    public AbstractUploadUtil uploadAbstractUtil(){
        //可以根据枚举进行配置 使用阿里云或者亚马逊S3或者Azure
        UploadServerEnum uploadServerEnum = UploadServerEnum.getEnum(uploadServerType);
        AbstractUploadUtil abstractUploadUtil;
        switch (uploadServerEnum){
            //亚马逊s3
            case AMAZON:
                s3accessKey = environment.getRequiredProperty("s3.accessKey");
                s3secretKey = environment.getRequiredProperty("s3.secretKey");
                s3endpoint = environment.getRequiredProperty("s3.endpoint");
                s3bucket = environment.getRequiredProperty("s3.bucket");
                abstractUploadUtil = new AmazonS3UploadUtil(basedir,s3accessKey,s3secretKey,s3endpoint,s3bucket);
                return abstractUploadUtil;
            //阿里云OSS
            case ALIOSS:
                aliyunAccessKey = environment.getRequiredProperty("aliyun.accessKey");
                aliyunSecretKey = environment.getRequiredProperty("aliyun.secretKey");
                aliyunBucket = environment.getRequiredProperty("aliyun.bucket");
                aliyunEndpoint = environment.getRequiredProperty("aliyun.endpoint");
                aliyunEndpointexternal = environment.getRequiredProperty("aliyun.endpointexternal");
                abstractUploadUtil = new AliOssUploadUtil(basedir,aliyunAccessKey,aliyunSecretKey,aliyunEndpoint,aliyunEndpointexternal,aliyunBucket);
                return abstractUploadUtil;
            //微软Azure
            case AZURE:
                azureAccountName = environment.getRequiredProperty("azure.accountName");
                azureAccountKey = environment.getRequiredProperty("azure.accountKey");
                azureEndpointSuffix = environment.getRequiredProperty("azure.endpointSuffix");
                azureContainerName = environment.getRequiredProperty("azure.containerName");
                abstractUploadUtil = new AzureUploadUtil(basedir,azureAccountName,azureAccountKey,azureEndpointSuffix,azureContainerName);
                return abstractUploadUtil;
            //腾讯云COS
            case TENCENTCOS:
                qAccessKey = environment.getRequiredProperty("tencent.accessKey");
                qSecretKey = environment.getRequiredProperty("tencent.secretKey");
                qBucket = environment.getRequiredProperty("tencent.bucket");
                qEndpoint = environment.getRequiredProperty("tencent.endpoint");
                qRegion = environment.getRequiredProperty("tencent.region");
                qEndpointExternal = environment.getRequiredProperty("tencent.endpointexternal");
                abstractUploadUtil = new TencentCOSUploadUtil(basedir,qAccessKey,qSecretKey,qBucket,qRegion,qEndpoint,qEndpointExternal);
                return abstractUploadUtil;
            //百度云BOS
            case BAIDUBOS:
                baiduAccessKey = environment.getRequiredProperty("baidu.accessKey");
                baiduSecretKey = environment.getRequiredProperty("baidu.secretKey");
                baiduBucket = environment.getRequiredProperty("baidu.bucket");
                baiduEndpoint = environment.getRequiredProperty("baidu.endpoint");
                baiduEndpointExternal = environment.getRequiredProperty("baidu.endpointexternal");
                abstractUploadUtil = new BaiduBOSUploadUtil(basedir,baiduAccessKey,baiduSecretKey,baiduEndpoint,baiduBucket,baiduEndpointExternal);
                return abstractUploadUtil;
            //七牛云存储
            case QINIUYUN:
                qiniuAccessKey = environment.getRequiredProperty("qiniu.accessKey");
                qiniuSecretKey = environment.getRequiredProperty("qiniu.secretKey");
                qiniuBucket = environment.getRequiredProperty("qiniu.bucket");
                qiniuEndpoint = environment.getRequiredProperty("qiniu.endpoint");
                abstractUploadUtil = new QiNiuOSSUploadUtil(basedir,qiniuAccessKey,qiniuSecretKey,qiniuEndpoint,qiniuBucket);
                return abstractUploadUtil;
            //华为云OBS
            case HUAWEIOBS:
                huaweiAccessKey = environment.getRequiredProperty("huawei.accessKey");
                huaweiSecretKey = environment.getRequiredProperty("huawei.secretKey");
                huaweiBucket = environment.getRequiredProperty("huawei.bucket");
                huaweiEndpoint = environment.getRequiredProperty("huawei.endpoint");
                abstractUploadUtil = new HuaweiOBSUploadUtil(basedir,huaweiAccessKey,huaweiSecretKey,huaweiEndpoint,huaweiBucket);
                return abstractUploadUtil;
            default:
                throw new RuntimeException("暂不支持其他类型的云上传！！！");
        }
    }
}
