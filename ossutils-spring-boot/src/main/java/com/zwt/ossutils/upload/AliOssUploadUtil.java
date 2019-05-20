package com.zwt.ossutils.upload;

import com.aliyun.oss.OSSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * @author zwt
 * @detail 阿里云上传
 * @date 2019/4/30
 * @since 1.0
 */
public class AliOssUploadUtil extends UploadAbstractUtil{

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
     * OSS Client
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
    }

    /**
     * 阿里云文件上传
     * @param tempFile
     * @param realName
     * @return
     */
    @Override
    public String upload(File tempFile,String realName){
        if(ossClient==null){
            ossClient = new OSSClient(aliyunendpoint, aliyunaccessKey, aliyunsecretKey);
        }
        ossClient.putObject(aliyunbucket, realName, tempFile);
        URL url = ossClient.generatePresignedUrl(aliyunbucket, realName, new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
        //ossClient.shutdown();
        String urlString = String.valueOf(url).split("\\?")[0];
        logger.info("阿里云OSS上传服务------图片内网url:[{}]", urlString);
        urlString = urlString.replaceAll(aliyunendpoint, aliyunendpointexternal);
        logger.info("阿里云OSS上传服务------图片外网url:[{}]", urlString);
        return urlString;
    }
}
